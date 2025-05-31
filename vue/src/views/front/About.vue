<template>
  <div class="about-page">
    <!-- Top banner -->
    <section class="hero-banner">
      <div class="container">
        <h1>About us</h1>
        <p>Explore the world and share the journey</p>
      </div>
    </section>

    <!-- Company introduction -->
    <section class="company-intro">
      <div class="container">
        <div class="row align-items-center">
          <div class="col-md-6">
            <h2>Our story</h2>
            <p>{{ companyInfo.story }}</p>
          </div>
          <div class="col-md-6">
            <img :src=img1 alt="">
          </div>
        </div>
      </div>
    </section>

    <!-- Mission and vision -->
    <section class="mission-vision">
      <div class="container">
        <div class="row">
          <div class="col-md-6">
            <div class="card">
              <div class="card-body">
                <h3>Our Mission</h3>
                <p>{{ companyInfo.mission }}</p>
              </div>
            </div>
          </div>
          <div class="col-md-6">
            <div class="card">
              <div class="card-body">
                <h3>Our vision</h3>
                <p>{{ companyInfo.vision }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Statistics -->
    <section class="stats-section">
      <div class="container">
        <div class="row">
          <div class="col-md-3" v-for="stat in statistics" :key="stat.id">
            <div class="stat-item">
              <h3><count-up :end-val="stat.value" :duration="2"></count-up>{{stat.value}}{{ stat.unit }}</h3>
              <p>{{ stat.label }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Legal Sections -->
    <section class="legal-sections" style="margin-left: 20px">
      <div class="container">
        <!-- Privacy Policy -->
        <div class="legal-card">
          <h2>Privacy Policy</h2>
          <p>Go2Go Travel Limited values your privacy. This policy explains how we collect, use, and protect your personal information when you interact with our services. We may collect your name, email address, phone number, booking details, and payment information when you use our website or contact us.</p>

          <h4>1. How We Use Your Information</h4>
          <ul>
            <li>To process bookings and payments</li>
            <li>To improve our services based on user feedback</li>
            <li>To respond to inquiries or customer service requests</li>
          </ul>

          <h4>2. Third Parties</h4>
          <p>We do not share your personal information with third parties except as required for payment processing or legal compliance.</p>

          <h4>3. Contact Us</h4>
          <p>If you have questions about this policy, please contact us at: support@go2gotravel.co.nz</p>
        </div>

        <!-- Trademark Statement -->
        <div class="legal-card">
          <h2>Trademark Statement</h2>
          <p>All trademarks, logos, and service marks displayed on this site are the property of Go2Go Travel Limited unless otherwise stated. Unauthorised use of any content, logos, or trademarks is strictly prohibited.</p>
        </div>

        <!-- Merchant Service Agreement -->
        <div class="legal-card">
          <h2>Merchant Service Agreement</h2>
          <p>This Merchant Service Agreement ("Agreement") is made between Go2Go Travel Limited and users ("Customers") of our booking platform.</p>

          <h4>1. Services Offered</h4>
          <p>We provide online quotation and booking services for tours, car rentals, and private transportation in New Zealand.</p>

          <h4>2. Booking Confirmation and Payment</h4>
          <p>Customers will receive a booking confirmation via email upon successful payment. Bookings are not guaranteed until full payment is received.</p>

          <h4>3. Refund & Cancellation Policy</h4>
          <p>Refunds are subject to the terms stated during booking. Cancellation fees may apply depending on the timing and type of service booked.</p>

          <h4>4. Changes to Agreement</h4>
          <p>Go2Go Travel reserves the right to modify this agreement. Any updates will be posted on our website.</p>
        </div>
      </div>
    </section>


    <!-- Contact us -->
    <section class="contact-section">
      <div class="container">
        <h2 class="text-center">Contact Us</h2>
        <div class="row">
          <div class="col-md-6">
            <form @submit.prevent="submitContactForm">
              <div class="mb-3">
                <label for="name" class="form-label">Name</label>
                <input type="text" class="form-control" id="name" v-model="contactForm.name" required>
              </div>
              <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" v-model="contactForm.email" required>
              </div>
              <div class="mb-3">
                <label for="message" class="form-label">Message</label>
                <textarea class="form-control" id="message" rows="5" v-model="contactForm.message" required></textarea>
              </div>
              <button type="submit" class="btn btn-primary">Send Message</button>
            </form>
          </div>
          <div class="col-md-6">
            <div class="contact-info">
              <h4>Contact Information</h4>
              <p><i class="fas fa-map-marker-alt"></i> {{ companyInfo.address }}</p>
              <p><i class="fas fa-phone"></i> {{ companyInfo.phone }}</p>
              <p><i class="fas fa-envelope"></i> {{ companyInfo.email }}</p>

              <h4 class="mt-4">Business Hours</h4>
              <p v-for="(time, day) in companyInfo.businessHours" :key="day">
                {{ day }}: {{ time }}
              </p>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue';
import img1 from "@/assets/imgs/company.png"


export default {
  data() {
    return {
      img1
    }
  },
  name: 'About',
  setup() {
    // Company information
    const companyInfo = reactive({
      story: 'We were founded in 2015, initially as a small local travel service provider. After years of development, we have grown into a global travel platform covering more than 50 countries, providing millions of travelers with high-quality travel products and services.',
      mission: 'To provide unique, high-quality travel products and services for global travelers, making every journey an unforgettable experience.',
      vision: 'To become the world\'s leading travel platform, connecting travelers with quality service providers and creating more travel possibilities.',
      address: '16 Victory street, Wellington, New Zealand',
      phone: '09-2891111',
      email: 'support@go2gotravel.co.nz',
      businessHours: {
        'Monday to Friday': '9:00 - 18:00',
        'Saturday': '10:00 - 16:00',
        'Sunday': 'Closed'
      }
    });

    // Team members
    const teamMembers = ref([
      {
        id: 1,
        name: 'Zhang Travel',
        position: 'Founder & CEO',
        avatar: '',
        social: {
          twitter: '#',
          linkedin: '#',
          instagram: '#'
        }
      },
      {
        id: 2,
        name: 'Li Explorer',
        position: 'Product Director',
        avatar: '',
        social: {
          twitter: '#',
          facebook: '#',
          dribbble: '#'
        }
      },
      // More team members...
    ]);

    // Statistics
    const statistics = ref([
      { id: 2, value: 50, unit: '+', label: 'Countries Covered' },
      { id: 3, value: 1000, unit: '+', label: 'Travel Products' },
      { id: 4, value: 1, unit: 'Years', label: 'Industry Experience' }
    ]);

    // Contact form
    const contactForm = reactive({
      name: '',
      email: '',
      message: ''
    });

    const submitContactForm = () => {
      // Add form submission logic here
      console.log('Form submitted:', contactForm);
      alert('Thank you for your message. We will reply to you as soon as possible!');
      // Reset form
      contactForm.name = '';
      contactForm.email = '';
      contactForm.message = '';
    };

    // Page load animation
    onMounted(() => {
      // Can add animation effects after page loads
    });

    return {
      companyInfo,
      teamMembers,
      statistics,
      contactForm,
      submitContactForm
    };
  }
};
</script>
<style scoped lang="scss">
.about-page {
  font-family: 'Arial', sans-serif;
  color: #333;

  .hero-banner {
    background: linear-gradient(rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.6)),
    url('https://example.com/images/support-banner.jpg');
    background-size: cover;
    background-position: center;
    color: white;
    padding: 120px 0;
    text-align: center;

    h1 {
      font-size: 3rem;
      font-weight: bold;
      margin-bottom: 20px;
    }

    p {
      font-size: 1.5rem;
    }
  }

  .company-intro, .mission-vision, .team-section, .stats-section, .contact-section {
    padding: 10px 0;



    p {
      padding-left: 10px;
    }

    img {
      padding-left: 10px;
    }

    h2 {
      padding-left: 10px;
      font-weight: bold;
      margin-bottom: 40px;
      position: relative;

      &::after {
        content: '';
        position: absolute;
        bottom: -10px;
        left: 0;
        width: 60px;
        height: 3px;
        background: #ff6b6b;
      }
    }
  }

  .mission-vision {
    background: #f8f9fa;

    .card {
      height: 100%;
      border: none;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
      transition: transform 0.3s;

      &:hover {
        transform: translateY(-5px);
      }

      h3 {
        color: #ff6b6b;
      }
    }
  }

  .team-section {
    .team-card {
      text-align: center;
      margin-bottom: 30px;

      img {
        border-radius: 50%;
        width: 150px;
        height: 150px;
        object-fit: cover;
        margin-bottom: 20px;
        border: 5px solid #fff;
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
      }

      h4 {
        margin-top: 15px;
      }

      .social-links {
        margin-top: 15px;

        a {
          color: #333;
          margin: 0 5px;
          font-size: 1.2rem;
          transition: color 0.3s;

          &:hover {
            color: #ff6b6b;
          }
        }
      }
    }
  }

  .stats-section {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;

    .stat-item {
      text-align: center;

      h3 {
        font-size: 3rem;
        font-weight: bold;
        margin-bottom: 10px;
      }

      p {
        font-size: 1.2rem;
        opacity: 0.9;
      }
    }
  }

  .contact-section {
    background: #f8f9fa;

    .contact-info {
      background: white;
      padding: 30px;
      border-radius: 5px;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);

      p {
        margin-bottom: 10px;

        i {
          color: #ff6b6b;
          margin-right: 10px;
          width: 20px;
          text-align: center;
        }
      }
    }

    form {
      background: white;
      padding: 30px;
      border-radius: 5px;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    }
  }
}

@media (max-width: 768px) {
  .about-page {
    .hero-banner {
      padding: 80px 0;

      h1 {
        font-size: 2rem;
      }

      p {
        font-size: 1.2rem;
      }
    }

    .company-intro, .mission-vision, .team-section, .stats-section, .contact-section {
      padding: 50px 0;
    }
  }
}
</style>