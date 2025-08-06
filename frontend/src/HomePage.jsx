import { useNavigate } from 'react-router-dom';
import './HomePage.css';

function HomePage() {
  const navigate = useNavigate();

  return (
    <div className="cozy-homepage">
      <div className="welcome-box">
        <h1 className="title">Hey there, welcome to ChatConnect 🌸</h1>

        <p className="intro-paragraph">
          This isn’t just another chat app. It’s a space I’ve built with love and thoughtfulness — a place where you can stay close to your people, share little moments, and build conversations that matter.
          Whether it’s a late-night chat with your closest friend, a group discussion that sparks ideas, or a quiet moment of reaching out to someone privately — this app is here to support all of that.
        </p>

        <p className="intro-paragraph">
          Every feature — from real-time messaging to email verification, private chats, group rooms, and refresh tokens — was added with one goal: to make your conversations feel natural, seamless, and secure.
        </p>

        <div className="button-group">
          <button className="btn primary" onClick={() => navigate('/login')}>
            🌟 Log In
          </button>
          <button className="btn secondary" onClick={() => navigate('/signup')}>
            ✨ Register
          </button>
        </div>
      </div>

      <footer className="footer">
        <p>Handcrafted with ❤️ by Devyani Gohil</p>
      </footer>
    </div>
  );
}

export default HomePage;
