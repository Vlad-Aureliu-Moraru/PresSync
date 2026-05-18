import { useState, FormEvent } from "react";
import { useNavigate } from "react-router";
import { Terminal, ChevronRight } from "lucide-react";

export default function Login() {
  const [isSignUp, setIsSignUp] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");
  const [surname, setSurname] = useState("");
  const [email, setEmail] = useState("");
  const [loading, setLoading] = useState(false);
  const [focusedField, setFocusedField] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleLogin = (e: FormEvent) => {
    e.preventDefault();
    setLoading(true);
    
    // Simulate login delay
    setTimeout(() => {
      setLoading(false);
      navigate("/dashboard", { state: { username, courseName: "CS-101 Advanced Programming" } });
    }, 1000);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 flex items-center justify-center p-4 font-mono relative overflow-hidden">
      {/* Subtle animated background elements */}
      <div className="absolute inset-0 overflow-hidden opacity-30">
        <div className="absolute top-20 left-20 w-96 h-96 bg-emerald-500/20 rounded-full blur-3xl animate-pulse"></div>
        <div className="absolute bottom-20 right-20 w-96 h-96 bg-blue-500/20 rounded-full blur-3xl animate-pulse delay-1000"></div>
      </div>

      <div className="w-full max-w-md relative z-10">
        {/* Header */}
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-emerald-500/10 backdrop-blur-xl rounded-2xl mb-4 border border-emerald-500/30 shadow-2xl">
            <Terminal className="w-8 h-8 text-emerald-400" />
          </div>
          <h1 className="text-3xl font-bold text-white mb-2">attendance.sys</h1>
          <p className="text-emerald-400/80 text-sm">
            <span className="text-emerald-500">$</span> initialize login sequence
          </p>
        </div>

        {/* Login Form */}
        <div className="bg-slate-900/40 backdrop-blur-2xl rounded-2xl shadow-2xl border border-slate-700/50 p-8 relative">
          {/* Terminal header bar */}
          <div className="absolute top-0 left-0 right-0 h-8 bg-slate-800/50 rounded-t-2xl border-b border-slate-700/50 flex items-center px-4 gap-2">
            <div className="w-3 h-3 rounded-full bg-red-500/70"></div>
            <div className="w-3 h-3 rounded-full bg-yellow-500/70"></div>
            <div className="w-3 h-3 rounded-full bg-emerald-500/70"></div>
            <span className="text-xs text-slate-500 ml-2">auth@attendance</span>
          </div>

          <form onSubmit={handleLogin} className="space-y-6 mt-8">
            {isSignUp && (
              <>
                {/* Name Field */}
                <div>
                  <label className="block text-sm font-medium text-emerald-400 mb-3 flex items-center gap-2">
                    <ChevronRight className="w-4 h-4" />
                    <span>name</span>
                    <span className="text-slate-600">// first name</span>
                  </label>
                  <div className="relative">
                    <span className="absolute left-4 top-1/2 -translate-y-1/2 text-emerald-500">$</span>
                    <input
                      type="text"
                      value={name}
                      onChange={(e) => setName(e.target.value)}
                      onFocus={() => setFocusedField('name')}
                      onBlur={() => setFocusedField(null)}
                      className="w-full bg-slate-950/50 backdrop-blur-xl border border-slate-700/50 rounded-xl pl-8 pr-4 py-3.5 text-white placeholder:text-slate-600 focus:outline-none focus:border-emerald-500/50 focus:ring-2 focus:ring-emerald-500/20 transition-all"
                      placeholder="enter_first_name"
                      required
                    />
                    {focusedField === 'name' && (
                      <div className="absolute right-4 top-1/2 -translate-y-1/2 w-2 h-5 bg-emerald-400 animate-pulse"></div>
                    )}
                  </div>
                </div>

                {/* Surname Field */}
                <div>
                  <label className="block text-sm font-medium text-emerald-400 mb-3 flex items-center gap-2">
                    <ChevronRight className="w-4 h-4" />
                    <span>surname</span>
                    <span className="text-slate-600">// last name</span>
                  </label>
                  <div className="relative">
                    <span className="absolute left-4 top-1/2 -translate-y-1/2 text-emerald-500">$</span>
                    <input
                      type="text"
                      value={surname}
                      onChange={(e) => setSurname(e.target.value)}
                      onFocus={() => setFocusedField('surname')}
                      onBlur={() => setFocusedField(null)}
                      className="w-full bg-slate-950/50 backdrop-blur-xl border border-slate-700/50 rounded-xl pl-8 pr-4 py-3.5 text-white placeholder:text-slate-600 focus:outline-none focus:border-emerald-500/50 focus:ring-2 focus:ring-emerald-500/20 transition-all"
                      placeholder="enter_last_name"
                      required
                    />
                    {focusedField === 'surname' && (
                      <div className="absolute right-4 top-1/2 -translate-y-1/2 w-2 h-5 bg-emerald-400 animate-pulse"></div>
                    )}
                  </div>
                </div>

                {/* Email Field */}
                <div>
                  <label className="block text-sm font-medium text-emerald-400 mb-3 flex items-center gap-2">
                    <ChevronRight className="w-4 h-4" />
                    <span>email</span>
                    <span className="text-slate-600">// contact address</span>
                  </label>
                  <div className="relative">
                    <span className="absolute left-4 top-1/2 -translate-y-1/2 text-emerald-500">$</span>
                    <input
                      type="email"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      onFocus={() => setFocusedField('email')}
                      onBlur={() => setFocusedField(null)}
                      className="w-full bg-slate-950/50 backdrop-blur-xl border border-slate-700/50 rounded-xl pl-8 pr-4 py-3.5 text-white placeholder:text-slate-600 focus:outline-none focus:border-emerald-500/50 focus:ring-2 focus:ring-emerald-500/20 transition-all"
                      placeholder="user@domain.com"
                      required
                    />
                    {focusedField === 'email' && (
                      <div className="absolute right-4 top-1/2 -translate-y-1/2 w-2 h-5 bg-emerald-400 animate-pulse"></div>
                    )}
                  </div>
                </div>
              </>
            )}

            {!isSignUp && (
              <>
                {/* Username Field */}
                <div>
                  <label className="block text-sm font-medium text-emerald-400 mb-3 flex items-center gap-2">
                    <ChevronRight className="w-4 h-4" />
                    <span>username</span>
                    <span className="text-slate-600">// who are you?</span>
                  </label>
                  <div className="relative">
                    <span className="absolute left-4 top-1/2 -translate-y-1/2 text-emerald-500">$</span>
                    <input
                      type="text"
                      value={username}
                      onChange={(e) => setUsername(e.target.value)}
                      onFocus={() => setFocusedField('username')}
                      onBlur={() => setFocusedField(null)}
                      className="w-full bg-slate-950/50 backdrop-blur-xl border border-slate-700/50 rounded-xl pl-8 pr-4 py-3.5 text-white placeholder:text-slate-600 focus:outline-none focus:border-emerald-500/50 focus:ring-2 focus:ring-emerald-500/20 transition-all"
                      placeholder="enter_username"
                      required
                    />
                    {focusedField === 'username' && (
                      <div className="absolute right-4 top-1/2 -translate-y-1/2 w-2 h-5 bg-emerald-400 animate-pulse"></div>
                    )}
                  </div>
                </div>
              </>
            )}

            {/* Password Field */}
            <div>
              <label className="block text-sm font-medium text-emerald-400 mb-3 flex items-center gap-2">
                <ChevronRight className="w-4 h-4" />
                <span>password</span>
                <span className="text-slate-600">// secure access key</span>
              </label>
              <div className="relative">
                <span className="absolute left-4 top-1/2 -translate-y-1/2 text-emerald-500">$</span>
                <input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  onFocus={() => setFocusedField('password')}
                  onBlur={() => setFocusedField(null)}
                  className="w-full bg-slate-950/50 backdrop-blur-xl border border-slate-700/50 rounded-xl pl-8 pr-4 py-3.5 text-white placeholder:text-slate-600 focus:outline-none focus:border-emerald-500/50 focus:ring-2 focus:ring-emerald-500/20 transition-all"
                  placeholder="••••••••"
                  required
                />
                {focusedField === 'password' && (
                  <div className="absolute right-4 top-1/2 -translate-y-1/2 w-2 h-5 bg-emerald-400 animate-pulse"></div>
                )}
              </div>
            </div>

            {/* Submit Button */}
            <button
              type="submit"
              disabled={loading}
              className="w-full bg-emerald-500/10 backdrop-blur-xl border border-emerald-500/30 text-emerald-400 rounded-xl px-4 py-3.5 font-medium hover:bg-emerald-500/20 hover:border-emerald-500/50 focus:outline-none focus:ring-2 focus:ring-emerald-500/30 transition-all disabled:opacity-50 disabled:cursor-not-allowed shadow-lg shadow-emerald-500/10 group"
            >
              <span className="flex items-center justify-center gap-2">
                {loading ? (
                  <>
                    <span className="inline-block w-2 h-2 bg-emerald-400 rounded-full animate-pulse"></span>
                    {isSignUp ? 'creating account...' : 'authenticating...'}
                  </>
                ) : (
                  <>
                    {isSignUp ? 'create account' : 'execute login'}
                    <ChevronRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
                  </>
                )}
              </span>
            </button>

            {/* Toggle Sign In / Login */}
            <div className="text-center pt-4 border-t border-slate-700/50">
              <button
                type="button"
                onClick={() => setIsSignUp(!isSignUp)}
                className="text-sm text-slate-400 hover:text-emerald-400 transition-colors"
              >
                {isSignUp ? (
                  <>
                    already have an account? <span className="text-emerald-400">login here</span>
                  </>
                ) : (
                  <>
                    need an account? <span className="text-emerald-400">sign up here</span>
                  </>
                )}
              </button>
            </div>
          </form>

          {/* Footer Info */}
          <div className="mt-6 pt-6 border-t border-slate-700/50">
            <div className="text-xs text-slate-500 space-y-1">
              <div className="flex justify-between">
                <span>status:</span>
                <span className="text-emerald-400">● online</span>
              </div>
              <div className="flex justify-between">
                <span>protocol:</span>
                <span className="text-slate-400">https/2.0</span>
              </div>
              <div className="flex justify-between">
                <span>session:</span>
                <span className="text-slate-400">guest</span>
              </div>
            </div>
          </div>
        </div>

        <p className="text-center text-xs text-slate-500 mt-6">
          v2.1.0 | secure attendance system © 2026
        </p>
      </div>
    </div>
  );
}