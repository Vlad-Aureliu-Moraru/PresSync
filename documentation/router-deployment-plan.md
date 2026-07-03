# PressSync Router Deployment Plan

## Goal

Auto-redirect any device that connects to the OpenWrt WiFi and opens a browser to the PressSync frontend, **every time** they connect. Internet (HTTPS, DNS, etc.) continues to work normally — no blocking.

## Architecture

```
[Device] --HTTP--> [OpenWrt Router (192.168.1.1)]
                         |
              nftables DNAT (port 80 → 4200)
              nftables SNAT (src → router IP)
                         |
                     [Laptop (192.168.1.240)]
                     +-- Frontend (port 4200) - Angular SPA
                     +-- Backend  (port 8080) - Spring Boot API
```

- **HTTP (port 80):** DNAT'd → laptop frontend (port 4200), SNAT'd → router IP
- **HTTPS (port 443) + everything else:** passes through normally → internet works

## Prerequisites

- Laptop IP (static DHCP): `192.168.1.240`
- Router SSH password: `vlad2005`
- Frontend is already built in `frontend/pressync-ui/dist/`
- Backend is a Spring Boot app in `backend/pressync/`

---

## Step 1 — Frontend API URL

**File:** `frontend/pressync-ui/src/environments/environment.ts`

```ts
apiUrl: 'http://192.168.1.240:8080'
```

Rebuild:

```bash
cd frontend/pressync-ui && npx ng build
```

---

## Step 2 — Serve Frontend & Backend

On the laptop:

```bash
# Frontend (port 4200)
npx http-server frontend/pressync-ui/dist/pressync-ui/browser \
  -p 4200 -a 0.0.0.0 --cors

# Backend (port 8080)
cd backend/pressync && ./mvnw spring-boot:run
```

---

## Step 3 — Router: DNAT (port 80 → laptop)

Applied via UCI (persistent across reboots):

```bash
uci add firewall redirect
uci set firewall.@redirect[-1].name="PressSync-HTTP-Redirect"
uci set firewall.@redirect[-1].src="lan"
uci set firewall.@redirect[-1].src_dport="80"
uci set firewall.@redirect[-1].proto="tcp"
uci set firewall.@redirect[-1].dest_ip="192.168.1.240"
uci set firewall.@redirect[-1].dest_port="4200"
uci set firewall.@redirect[-1].target="DNAT"
uci commit firewall
```

---

## Step 4 — Router: SNAT (source rewrite for DNAT traffic)

Without this, the client's TCP stack rejects responses because the source IP doesn't match what it originally connected to.

**Script** (`/etc/firewall.pressync`):
```bash
#!/bin/sh
nft add rule inet fw4 srcnat oifname "br-lan" ct status dnat masquerade
```

**UCI include** (runs on every firewall reload):
```bash
uci add firewall include
uci set firewall.@include[-1].path="/etc/firewall.pressync"
uci set firewall.@include[-1].type="script"
uci commit firewall
```

**Boot persistence** (in `/etc/rc.local`):
```bash
nft add rule inet fw4 srcnat oifname "br-lan" ct status dnat masquerade
```

---

## Step 5 — DNS Redirect for Captive Portal Checks

Forces phones to show the "Sign in to network" popup every reconnect by intercepting their check domains:

```bash
uci add_list dhcp.@dnsmasq[0].address="/captive.apple.com/192.168.1.1"
uci add_list dhcp.@dnsmasq[0].address="/connectivitycheck.gstatic.com/192.168.1.1"
uci add_list dhcp.@dnsmasq[0].address="/www.msftconnecttest.com/192.168.1.1"
uci add_list dhcp.@dnsmasq[0].address="/clients3.google.com/generate_204/192.168.1.1"
uci add_list dhcp.@dnsmasq[0].address="/nmcheck.gnome.org/192.168.1.1"
uci commit dhcp
/etc/init.d/dnsmasq restart
```

Also add DHCP option 114 (captive portal hint) — tells phones to open the browser on connect:

```bash
uci add_list dhcp.lan.dhcp_option="114,http://192.168.1.1/"
uci commit dhcp
/etc/init.d/dnsmasq reload
```

---

## Step 6 — Disable OpenNDS

```bash
/etc/init.d/opennds stop
/etc/init.d/opennds disable
```

---

## Verify

Check the rules on the router:

```bash
ssh root@192.168.1.1
nft list chain inet fw4 dstnat_lan
# Should show: tcp dport 80 dnat ip to 192.168.1.240:4200

nft list chain inet fw4 srcnat
# Should show: oifname "br-lan" ct status dnat masquerade
```

Test from any device: visit `http://192.168.1.1/` — should show PressSync.

---

## How It Works

1. User connects to WiFi → phone gets DHCP lease + option 114
2. Phone auto-opens browser to captive portal check → hits router on port 80
3. Router DNATs → request goes to laptop frontend (port 4200)
4. Router SNATs → source IP rewritten to router (192.168.1.1)
5. Laptop responds → router un-NATs → phone sees response from expected IP
6. User sees PressSync, closes tab → all other traffic (HTTPS) works normally
7. Disconnect + reconnect → same flow repeats

## Troubleshooting

| Symptom | Likely Cause | Fix |
|---|---|---|
| Frontend loads but API calls fail | `apiUrl` still points to `localhost` | Rebuild with `192.168.1.240:8080` |
| No redirect | Firewall rules not applied | Run `/etc/init.d/firewall reload` on router |
| Some sites load as raw HTML | Those sites use HTTP (not HTTPS) | Normal — only HTTPS passes through untouched |
| Frontend not reachable | Laptop server not running | Check `http-server` or backend process |
