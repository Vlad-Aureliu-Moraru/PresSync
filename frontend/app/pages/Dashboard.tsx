import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router";
import { CheckCircle, XCircle, Calendar, TrendingUp, LogOut, X, Terminal, Activity, ChevronRight, Search } from "lucide-react";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from "recharts";

interface LocationState {
  username?: string;
  courseName?: string;
}

export default function Dashboard() {
  const location = useLocation();
  const navigate = useNavigate();
  const { username = "Guest", courseName = "Unknown Course" } = (location.state as LocationState) || {};
  const [showNotification, setShowNotification] = useState(true);
  const [currentTime, setCurrentTime] = useState(new Date());
  const [searchQuery, setSearchQuery] = useState("");
  const [timeFrame, setTimeFrame] = useState<"daily" | "weekly" | "monthly" | "yearly">("weekly");

  // Mock attendance data based on timeframe
  const getAttendanceData = () => {
    switch (timeFrame) {
      case "daily":
        return [
          { period: "Mon", attended: 2, missed: 0 },
          { period: "Tue", attended: 1, missed: 1 },
          { period: "Wed", attended: 2, missed: 0 },
          { period: "Thu", attended: 2, missed: 0 },
          { period: "Fri", attended: 1, missed: 0 },
          { period: "Sat", attended: 0, missed: 1 },
          { period: "Sun", attended: 0, missed: 0 },
        ];
      case "weekly":
        return [
          { period: "W1", attended: 1, missed: 0 },
          { period: "W2", attended: 2, missed: 0 },
          { period: "W3", attended: 1, missed: 1 },
          { period: "W4", attended: 2, missed: 0 },
          { period: "W5", attended: 1, missed: 0 },
          { period: "W6", attended: 2, missed: 0 },
          { period: "W7", attended: 1, missed: 1 },
          { period: "W8", attended: 2, missed: 0 },
        ];
      case "monthly":
        return [
          { period: "Jan", attended: 8, missed: 2 },
          { period: "Feb", attended: 7, missed: 1 },
          { period: "Mar", attended: 9, missed: 1 },
          { period: "Apr", attended: 8, missed: 2 },
          { period: "May", attended: 10, missed: 0 },
          { period: "Jun", attended: 7, missed: 3 },
        ];
      case "yearly":
        return [
          { period: "2022", attended: 45, missed: 5 },
          { period: "2023", attended: 52, missed: 8 },
          { period: "2024", attended: 58, missed: 2 },
          { period: "2025", attended: 60, missed: 0 },
          { period: "2026", attended: 12, missed: 2 },
        ];
      default:
        return [];
    }
  };

  const attendanceData = getAttendanceData();

  const totalAttended = attendanceData.reduce((sum, item) => sum + item.attended, 0);
  const totalMissed = attendanceData.reduce((sum, item) => sum + item.missed, 0);
  const totalClasses = totalAttended + totalMissed;
  const attendanceRate = totalClasses > 0 ? ((totalAttended / totalClasses) * 100).toFixed(1) : "0.0";

  useEffect(() => {
    // Hide notification after 5 seconds
    const timer = setTimeout(() => {
      setShowNotification(false);
    }, 5000);

    return () => clearTimeout(timer);
  }, []);

  useEffect(() => {
    // Update time every second
    const timer = setInterval(() => {
      setCurrentTime(new Date());
    }, 1000);

    return () => clearInterval(timer);
  }, []);

  const handleLogout = () => {
    navigate("/");
  };

  const getStatusEmoji = () => {
    const rate = parseFloat(attendanceRate);
    if (rate >= 90) return "🔥";
    if (rate >= 75) return "💪";
    if (rate >= 60) return "👍";
    return "🚀";
  };

  const getStatusMessage = () => {
    const rate = parseFloat(attendanceRate);
    if (rate >= 90) return "excellent performance";
    if (rate >= 75) return "great progress";
    if (rate >= 60) return "steady improvement";
    return "let's level up";
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 font-mono relative overflow-hidden">
      {/* Subtle animated background */}
      <div className="absolute inset-0 overflow-hidden opacity-20">
        <div className="absolute top-20 left-20 w-96 h-96 bg-emerald-500/30 rounded-full blur-3xl animate-pulse"></div>
        <div className="absolute bottom-20 right-20 w-96 h-96 bg-blue-500/30 rounded-full blur-3xl animate-pulse delay-1000"></div>
      </div>

      {/* Header */}
      <header className="bg-slate-900/60 backdrop-blur-2xl border-b border-slate-700/50 sticky top-0 z-10 shadow-2xl">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
          <div className="flex items-center justify-between gap-4 flex-wrap">
            <div className="flex items-center gap-4">
              <div className="w-10 h-10 bg-emerald-500/10 backdrop-blur-xl rounded-xl flex items-center justify-center border border-emerald-500/30">
                <Terminal className="w-5 h-5 text-emerald-400" />
              </div>
              <div>
                <h1 className="text-lg font-bold text-white flex items-center gap-2">
                  attendance.sys
                  <Activity className="w-4 h-4 text-emerald-400 animate-pulse" />
                </h1>
                <p className="text-xs text-slate-500">
                  <span className="text-emerald-500">$</span> user: {username} | {currentTime.toLocaleTimeString()}
                </p>
              </div>
            </div>

            {/* Search Bar */}
            <div className="flex items-center gap-2 flex-1 max-w-md">
              <div className="relative flex-1">
                <span className="absolute left-4 top-1/2 -translate-y-1/2 text-emerald-500">$</span>
                <input
                  type="text"
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="w-full bg-slate-950/50 backdrop-blur-xl border border-slate-700/50 rounded-lg pl-8 pr-4 py-2 text-sm text-white placeholder:text-slate-600 focus:outline-none focus:border-emerald-500/50 focus:ring-2 focus:ring-emerald-500/20 transition-all"
                  placeholder="search_records"
                />
              </div>
              <button
                className="flex items-center gap-2 px-4 py-2 text-sm text-emerald-400 bg-emerald-500/10 backdrop-blur-xl hover:bg-emerald-500/20 rounded-lg transition-all border border-emerald-500/30 shadow-lg shadow-emerald-500/10"
              >
                <Search className="w-4 h-4" />
                search
              </button>
            </div>

            <button
              onClick={handleLogout}
              className="flex items-center gap-2 px-4 py-2 text-sm text-emerald-400 bg-slate-800/50 backdrop-blur-xl hover:bg-slate-800/80 rounded-lg transition-all border border-slate-700/50"
            >
              <LogOut className="w-4 h-4" />
              exit
            </button>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 relative z-10">
        {/* Login Notification */}
        {showNotification && (
          <div className="mb-6 bg-emerald-950/30 backdrop-blur-2xl border border-emerald-500/30 rounded-2xl p-5 shadow-xl animate-in fade-in slide-in-from-top-2 duration-300">
            <div className="flex items-start justify-between">
              <div className="flex items-start gap-4">
                <div className="w-10 h-10 bg-emerald-500/20 rounded-xl flex items-center justify-center flex-shrink-0 border border-emerald-500/30">
                  <CheckCircle className="w-5 h-5 text-emerald-400" />
                </div>
                <div>
                  <h3 className="font-bold text-emerald-400 mb-1 flex items-center gap-2">
                    <ChevronRight className="w-4 h-4" />
                    authentication successful 🎉
                  </h3>
                  <p className="text-slate-300 text-sm">
                    logged into <span className="text-emerald-400">{courseName}</span>
                  </p>
                  <p className="text-slate-500 text-xs mt-1">session started at {currentTime.toLocaleTimeString()}</p>
                </div>
              </div>
              <button
                onClick={() => setShowNotification(false)}
                className="text-slate-500 hover:text-slate-300 transition-colors"
              >
                <X className="w-5 h-5" />
              </button>
            </div>
          </div>
        )}

        {/* Status Banner */}
        <div className="mb-6 bg-slate-900/40 backdrop-blur-2xl border border-slate-700/50 rounded-2xl p-5 shadow-xl relative overflow-hidden">
          <div className="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r from-emerald-500/50 via-blue-500/50 to-purple-500/50"></div>
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <span className="text-3xl">{getStatusEmoji()}</span>
              <div>
                <h3 className="font-bold text-white text-lg">
                  status: <span className="text-emerald-400">{getStatusMessage()}</span>
                </h3>
                <p className="text-slate-400 text-sm">keep up the momentum!</p>
              </div>
            </div>
            <div className="text-right">
              <div className="text-xs text-slate-500">attendance_rate</div>
              <div className="text-2xl font-bold text-emerald-400">{attendanceRate}%</div>
            </div>
          </div>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
          <div className="bg-slate-900/40 backdrop-blur-2xl rounded-2xl p-6 shadow-xl border border-slate-700/50 hover:border-blue-500/50 transition-all group cursor-pointer">
            <div className="flex items-center justify-between mb-3">
              <div className="w-10 h-10 bg-blue-500/10 rounded-xl flex items-center justify-center border border-blue-500/30 group-hover:bg-blue-500/20 transition-all">
                <Calendar className="w-5 h-5 text-blue-400" />
              </div>
              <span className="text-xs text-slate-500">total</span>
            </div>
            <p className="text-3xl font-bold text-white mb-1">{totalClasses}</p>
            <p className="text-xs text-slate-500">classes logged</p>
          </div>

          <div className="bg-slate-900/40 backdrop-blur-2xl rounded-2xl p-6 shadow-xl border border-slate-700/50 hover:border-emerald-500/50 transition-all group cursor-pointer">
            <div className="flex items-center justify-between mb-3">
              <div className="w-10 h-10 bg-emerald-500/10 rounded-xl flex items-center justify-center border border-emerald-500/30 group-hover:bg-emerald-500/20 transition-all">
                <CheckCircle className="w-5 h-5 text-emerald-400" />
              </div>
              <span className="text-xs text-slate-500">present</span>
            </div>
            <p className="text-3xl font-bold text-white mb-1">{totalAttended}</p>
            <p className="text-xs text-emerald-500">nice work! ✨</p>
          </div>

          <div className="bg-slate-900/40 backdrop-blur-2xl rounded-2xl p-6 shadow-xl border border-slate-700/50 hover:border-red-500/50 transition-all group cursor-pointer">
            <div className="flex items-center justify-between mb-3">
              <div className="w-10 h-10 bg-red-500/10 rounded-xl flex items-center justify-center border border-red-500/30 group-hover:bg-red-500/20 transition-all">
                <XCircle className="w-5 h-5 text-red-400" />
              </div>
              <span className="text-xs text-slate-500">absent</span>
            </div>
            <p className="text-3xl font-bold text-white mb-1">{totalMissed}</p>
            <p className="text-xs text-slate-500">it happens 😅</p>
          </div>

          <div className="bg-slate-900/40 backdrop-blur-2xl rounded-2xl p-6 shadow-xl border border-slate-700/50 hover:border-purple-500/50 transition-all group cursor-pointer">
            <div className="flex items-center justify-between mb-3">
              <div className="w-10 h-10 bg-purple-500/10 rounded-xl flex items-center justify-center border border-purple-500/30 group-hover:bg-purple-500/20 transition-all">
                <TrendingUp className="w-5 h-5 text-purple-400" />
              </div>
              <span className="text-xs text-slate-500">rate</span>
            </div>
            <p className="text-3xl font-bold text-white mb-1">{attendanceRate}%</p>
            <p className="text-xs text-purple-400">tracking well 📈</p>
          </div>
        </div>

        {/* Chart */}
        <div className="bg-slate-900/40 backdrop-blur-2xl rounded-2xl p-6 shadow-xl border border-slate-700/50 relative">
          {/* Terminal header */}
          <div className="absolute top-0 left-0 right-0 h-8 bg-slate-800/50 rounded-t-2xl border-b border-slate-700/50 flex items-center px-4 gap-2">
            <div className="w-2 h-2 rounded-full bg-emerald-500/70"></div>
            <span className="text-xs text-slate-500">analytics.dashboard</span>
          </div>

          <div className="mt-8 mb-6">
            <div className="flex items-center justify-between flex-wrap gap-4 mb-3">
              <h2 className="text-xl font-bold text-white flex items-center gap-2">
                <ChevronRight className="w-5 h-5 text-emerald-400" />
                {timeFrame}_attendance_chart
              </h2>

              {/* Time Frame Dropdown */}
              <div className="flex items-center gap-2">
                <span className="text-sm text-slate-500">view:</span>
                <select
                  value={timeFrame}
                  onChange={(e) => setTimeFrame(e.target.value as "daily" | "weekly" | "monthly" | "yearly")}
                  className="bg-slate-950/50 backdrop-blur-xl border border-slate-700/50 rounded-lg px-4 py-2 text-sm text-emerald-400 focus:outline-none focus:border-emerald-500/50 focus:ring-2 focus:ring-emerald-500/20 transition-all cursor-pointer hover:bg-slate-900/50"
                >
                  <option value="daily" className="bg-slate-900 text-emerald-400">daily</option>
                  <option value="weekly" className="bg-slate-900 text-emerald-400">weekly</option>
                  <option value="monthly" className="bg-slate-900 text-emerald-400">monthly</option>
                  <option value="yearly" className="bg-slate-900 text-emerald-400">yearly</option>
                </select>
              </div>
            </div>
            <p className="text-sm text-slate-500">
              <span className="text-emerald-500">$</span> visualizing your progress over time
            </p>
          </div>

          <div className="h-80 bg-slate-950/50 backdrop-blur-xl rounded-xl p-4 border border-slate-700/30">
            <ResponsiveContainer width="100%" height="100%" key={timeFrame}>
              <BarChart data={attendanceData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#334155" vertical={false} />
                <XAxis
                  dataKey="period"
                  stroke="#64748b"
                  style={{ fontSize: '12px', fontFamily: 'monospace' }}
                  tickLine={false}
                  axisLine={false}
                />
                <YAxis
                  stroke="#64748b"
                  style={{ fontSize: '12px', fontFamily: 'monospace' }}
                  tickLine={false}
                  axisLine={false}
                />
                <Tooltip
                  contentStyle={{
                    backgroundColor: 'rgba(15, 23, 42, 0.95)',
                    backdropFilter: 'blur(20px)',
                    border: '1px solid rgba(100, 116, 139, 0.5)',
                    borderRadius: '12px',
                    color: '#fff',
                    fontFamily: 'monospace',
                    fontSize: '12px',
                  }}
                  cursor={{ fill: 'rgba(100, 116, 139, 0.1)' }}
                />
                <Bar dataKey="attended" stackId="a" fill="#34d399" radius={[4, 4, 0, 0]} name="attended" />
                <Bar dataKey="missed" stackId="a" fill="#f87171" radius={[4, 4, 0, 0]} name="missed" />
              </BarChart>
            </ResponsiveContainer>
          </div>

          <div className="mt-6 flex gap-6 justify-center">
            <div className="flex items-center gap-2 bg-slate-800/50 px-4 py-2 rounded-lg border border-slate-700/50">
              <div className="w-3 h-3 bg-emerald-400 rounded"></div>
              <span className="text-xs text-slate-400">attended ✓</span>
            </div>
            <div className="flex items-center gap-2 bg-slate-800/50 px-4 py-2 rounded-lg border border-slate-700/50">
              <div className="w-3 h-3 bg-red-400 rounded"></div>
              <span className="text-xs text-slate-400">missed ✗</span>
            </div>
          </div>
        </div>

        {/* Footer */}
        <div className="mt-6 text-center text-xs text-slate-600">
          <div className="flex items-center justify-center gap-4">
            <span>status: <span className="text-emerald-400">● active</span></span>
            <span>|</span>
            <span>last_update: {currentTime.toLocaleTimeString()}</span>
            <span>|</span>
            <span>v2.1.0</span>
          </div>
        </div>
      </main>
    </div>
  );
}