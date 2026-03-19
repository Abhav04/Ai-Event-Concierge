import { useEffect, useState } from "react";
import axios from "axios";
import "./App.css";

function App() {
  const [prompt, setPrompt] = useState("");
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchHistory();
  }, []);

  const fetchHistory = async () => {
    try {
      const res = await axios.get(`${process.env.REACT_APP_API_URL}/api/history`);
      setEvents(res.data.reverse());
    } catch (err) {
      console.error("Error fetching history", err);
    }
  };

  const generateEvent = async () => {
    if (!prompt.trim()) return;
    try {
      setLoading(true);
      await axios.post(
  `${process.env.REACT_APP_API_URL}/api/generate-event`,
        JSON.stringify(prompt),
        { headers: { "Content-Type": "application/json" } }
      );
      setPrompt("");
      fetchHistory();
    } catch (err) {
      console.error("Error generating event", err);
    } finally {
      setLoading(false);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter" && !loading) generateEvent();
  };

  return (
    <div className="app">
      {/* ── Header ── */}
      <header className="header">
        <p className="header-eyebrow">Powered by AI</p>
        <h1 className="header-title">
          Event <em>Concierge</em>
        </h1>
        <p className="header-subtitle">
          Describe your vision. We'll find the perfect venue, estimate costs,<br />
          and explain why it fits — instantly.
        </p>
      </header>

      {/* ── Search ── */}
      <section className="search-section">
        <label className="search-label" htmlFor="event-input">
          Describe your event
        </label>
        <div className="search-row">
          <input
            id="event-input"
            className="search-input"
            value={prompt}
            onChange={(e) => setPrompt(e.target.value)}
            onKeyDown={handleKeyDown}
            placeholder="e.g. Tech summit for 500 in Delhi, rooftop cocktail party in Mumbai…"
            disabled={loading}
          />
          <div className="search-divider" />
          <button
            className="search-btn"
            onClick={generateEvent}
            disabled={loading || !prompt.trim()}
          >
            {loading ? "Planning…" : "Generate"}
          </button>
        </div>
        <p className="search-hint">Press Enter or click Generate</p>

        {loading && (
          <div className="loading-state">
            <div className="loading-dots">
              <span /><span /><span />
            </div>
            <span className="loading-text">AI is curating your event…</span>
          </div>
        )}
      </section>

      {/* ── History ── */}
      <section>
        <div className="history-header">
          <span className="history-title">Proposals</span>
          <span className="history-count">{events.length}</span>
          <div className="history-line" />
        </div>

        {events.length === 0 && !loading ? (
          <div className="empty-state">
            <div className="empty-icon">✦</div>
            <p className="empty-text">No proposals yet</p>
            <p className="empty-sub">Your curated event history will appear here</p>
          </div>
        ) : (
          <div className="events-list">
            {events.map((e, index) => (
              <div className="event-card" key={index}>
                <div className="event-card-top">
                  <p className="event-prompt">
                    "{e.userPrompt?.replace(/"/g, "")}"
                  </p>
                  <span className="event-badge">Proposal</span>
                </div>

                <div className="event-card-meta">
                  <div className="meta-item">
                    <p className="meta-label">Venue</p>
                    <p className="meta-value">{e.venueName}</p>
                  </div>
                  <div className="meta-item">
                    <p className="meta-label">Location</p>
                    <p className="meta-value">{e.location}</p>
                  </div>
                  <div className="meta-item" style={{ gridColumn: "1 / -1" }}>
                    <p className="meta-label">Estimated Cost</p>
                    <p className="meta-value cost">{e.estimatedCost}</p>
                  </div>
                </div>

                <div className="event-card-why">
                  <p className="why-label">Why it fits</p>
                  <p className="why-text">{e.whyItFits}</p>
                </div>
              </div>
            ))}
          </div>
        )}
      </section>
    </div>
  );
}

export default App;