// Home.tsx
import React from "react";
import styles from "./home.module.css";
import revupIcon from "../../assets/revup-icon.jpg";

const Home: React.FC = () => {
  return (
    <div className={styles.page}>
      {/* Header */}
      <header className={styles.header}>
        <div className={styles.logo}>
          <div className={styles.logoIcon}>
            <img
              src={revupIcon}
              alt="RevUp Logo"
              className={styles.logoImage}
            />
          </div>
          <span>RevUp</span>
        </div>
        <nav className={styles.nav}>
          <a className={styles.navLink}>Customer Dashboard</a>
          <a className={styles.navLink}>Technician Portal</a>
          <a className={styles.navLink}>Admin Panel</a>
          <a className={styles.navLink}>Sign In</a>
          <button className={styles.btnPrimary}>Get Started</button>
        </nav>
      </header>

      {/* Hero Section */}
      <section className={styles.hero}>
        <div className={styles.heroContent}>
          <h1 className={styles.heroTitle}>
            The complete platform to{" "}
            <span className={styles.heroTitleAccent}>revolutionize</span> auto
            service.
          </h1>
          <p className={styles.heroDescription}>
            Streamline your service center operations with real-time tracking,
            automated scheduling, and seamless customer communication. Join the
            automotive service revolution.
          </p>
          <div className={styles.heroButtons}>
            <button className={styles.btnPrimary}>Get Started</button>
            <button className={styles.btnSecondary}>Watch Demo</button>
          </div>
        </div>

        <div className={styles.dashboard}>
          <div className={styles.dashboardHeader}>Service Dashboard</div>
          <div className={styles.cards}>
            <div className={`${styles.card} ${styles.cardOil}`}>
              <div className={styles.cardIcon}>🛢️</div>
              <div className={styles.cardTitle}>Oil Change</div>
              <div className={styles.cardValue}>45 min</div>
            </div>
            <div className={`${styles.card} ${styles.cardTire}`}>
              <div className={styles.cardIcon}>🔧</div>
              <div className={styles.cardTitle}>Tire Rotation</div>
              <div className={styles.cardValue}>30 min</div>
            </div>
          </div>
          <div className={styles.statsBar}>
            <span className={styles.statsBarIcon}>✓ Live</span>
            <span className={styles.statsBarText}>
              100% assigned • Customer notified
            </span>
          </div>
        </div>
      </section>

      {/* Stats Section */}
      <section className={styles.stats}>
        <div className={styles.statItem}>
          <div className={styles.statValue}>100+</div>
          <div className={styles.statLabel}>Vehicles per week</div>
        </div>
        <div className={styles.statItem}>
          <div className={styles.statValue}>50%</div>
          <div className={styles.statLabel}>Faster service</div>
        </div>
        <div className={styles.statItem}>
          <div className={styles.statValue}>95%</div>
          <div className={styles.statLabel}>Customer satisfaction</div>
        </div>
        <div className={styles.statItem}>
          <div className={styles.statValue}>24/7</div>
          <div className={styles.statLabel}>Real-time tracking</div>
        </div>
      </section>

      {/* Features Section */}
      <section className={styles.features}>
        <div className={styles.featuresHeader}>
          <h2 className={styles.featuresTitle}>
            Everything you need to manage your service center
          </h2>
          <p className={styles.featuresSubtitle}>
            From appointment scheduling to service completion, Revify provides
            all the tools your team needs to deliver exceptional customer
            service.
          </p>
        </div>
        <div className={styles.featuresGrid}>
          <div className={styles.featureCard}>
            <div className={styles.featureIcon}>📅</div>
            <h3 className={styles.featureTitle}>Smart Scheduling</h3>
            <p className={styles.featureDescription}>
              Intelligent appointment scheduling with real-time availability and
              customer notifications.
            </p>
          </div>
          <div className={styles.featureCard}>
            <div className={styles.featureIcon}>⏱️</div>
            <h3 className={styles.featureTitle}>Time Tracking</h3>
            <p className={styles.featureDescription}>
              Precise service time tracking with progress updates and completion
              notifications.
            </p>
          </div>
          <div className={styles.featureCard}>
            <div className={styles.featureIcon}>🔔</div>
            <h3 className={styles.featureTitle}>Real-time Updates</h3>
            <p className={styles.featureDescription}>
              Instant notifications keep customers informed throughout the
              service process.
            </p>
          </div>
          <div className={styles.featureCard}>
            <div className={styles.featureIcon}>👥</div>
            <h3 className={styles.featureTitle}>Team Management</h3>
            <p className={styles.featureDescription}>
              Easily manage technicians, track workloads, and monitor team
              performance.
            </p>
          </div>
          <div className={styles.featureCard}>
            <div className={styles.featureIcon}>📊</div>
            <h3 className={styles.featureTitle}>Analytics Dashboard</h3>
            <p className={styles.featureDescription}>
              Comprehensive insights into service metrics, revenue, and customer
              satisfaction.
            </p>
          </div>
          <div className={styles.featureCard}>
            <div className={styles.featureIcon}>🔒</div>
            <h3 className={styles.featureTitle}>Secure & Reliable</h3>
            <p className={styles.featureDescription}>
              Enterprise-grade security with reliable uptime and automated
              backups.
            </p>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className={styles.cta}>
        <h2 className={styles.ctaTitle}>
          Ready to revolutionize your auto service?
        </h2>
        <p className={styles.ctaSubtitle}>
          Join hundreds of service centers already using Revify to streamline
          operations and delight customers.
        </p>
        <button className={`${styles.btnPrimary} ${styles.btnLarge}`}>
          Get Started Today
        </button>
      </section>

      {/* Footer */}
      <footer className={styles.footer}>
        <div className={styles.logo}>
          <div className={styles.logoIcon}>
            <img
              src={revupIcon}
              alt="RevUp Logo"
              className={styles.logoImage}
            />
          </div>
          <span>RevUp</span>
        </div>
        <div className={styles.footerText}>
          © 2025 Revify. All rights reserved.
        </div>
      </footer>
    </div>
  );
};

export default Home;
