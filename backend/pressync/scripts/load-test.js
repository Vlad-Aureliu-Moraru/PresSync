import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

const BASE_URL = __ENV.API_URL || 'http://localhost:8080';
const failureRate = new Rate('failed_requests');

export const options = {
  stages: [
    { duration: '30s', target: 10 },
    { duration: '1m', target: 20 },
    { duration: '30s', target: 0 },
  ],
  thresholds: {
    failed_requests: ['rate<0.05'],
    http_req_duration: ['p(95)<2000'],
  },
};

export default function () {
  const registerPayload = JSON.stringify({
    name: 'LoadTest',
    surname: 'User',
    email: `loadtest${__VU}@test.com`,
    password: 'testpass123',
  });

  const registerRes = http.post(`${BASE_URL}/auth/register`, registerPayload, {
    headers: { 'Content-Type': 'application/json' },
  });

  check(registerRes, {
    'register status 200': (r) => r.status === 200,
  }) || failureRate.add(1);

  const loginPayload = JSON.stringify({
    email: `loadtest${__VU}@test.com`,
    password: 'testpass123',
  });

  const loginRes = http.post(`${BASE_URL}/auth/login`, loginPayload, {
    headers: { 'Content-Type': 'application/json' },
  });

  const loginOk = check(loginRes, {
    'login status 200': (r) => r.status === 200,
  }) || failureRate.add(1);

  if (loginOk) {
    try {
      const token = loginRes.json('token');
      const headers = {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      };

      const categoriesRes = http.get(`${BASE_URL}/eventCategory`, { headers });
      check(categoriesRes, {
        'category list status 200': (r) => r.status === 200,
      }) || failureRate.add(1);

      const markRes = http.post(`${BASE_URL}/attendance/mark`, null, { headers });
      check(markRes, {
        'mark attendance ok or conflict': (r) => r.status === 200 || r.status === 400,
      }) || failureRate.add(1);
    } catch (e) {
      failureRate.add(1);
    }
  }

  sleep(1);
}
