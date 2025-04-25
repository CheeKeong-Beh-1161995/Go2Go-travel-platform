<template>
  <div class="support-page">
    <!-- Top banner -->
    <section class="support-hero">
      <div class="container">
        <h1>Customer Support</h1>
        <p>We're here to help you anytime</p>
      </div>
    </section>

    <!-- Support options -->
    <section class="support-options">
      <div class="container">
        <div class="row">
          <div class="col-md-4" v-for="option in supportOptions" :key="option.id">
            <div class="support-card" @click="scrollToSection(option.target)">
              <div class="icon-wrapper">
                <i :class="option.icon"></i>
              </div>
              <h3>{{ option.title }}</h3>
              <p>{{ option.description }}</p>
              <a href="#" class="link-arrow">Learn more <i class="fas fa-arrow-right"></i></a>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Frequently Asked Questions -->
    <section class="faq-section" id="faq" ref="faqSection">
      <div class="container">
        <div class="section-header">
          <h2>Frequently Asked Questions</h2>
          <div class="search-box">
            <input
                type="text"
                placeholder="Search questions..."
                v-model="searchQuery"
                @input="filterFAQs"
            >
            <i class="fas fa-search"></i>
          </div>
        </div>

        <div class="faq-categories">
          <button
              v-for="category in categories"
              :key="category.id"
              :class="{ active: activeCategory === category.id }"
              @click="filterByCategory(category.id)"
          >
            {{ category.name }}
          </button>
        </div>

        <div class="faq-accordion">
          <div
              class="faq-item"
              v-for="(faq, index) in filteredFAQs"
              :key="faq.id"
              :class="{ active: activeFAQ === index }"
          >
            <div class="faq-question" @click="toggleFAQ(index)">
              <h3>{{ faq.question }}</h3>
              <i :class="['fas', activeFAQ === index ? 'fa-minus' : 'fa-plus']"></i>
            </div>
            <div class="faq-answer" v-show="activeFAQ === index">
              <p>{{ faq.answer }}</p>
              <div class="faq-helpful" v-if="faq.helpful">
                <span>Was this answer helpful?</span>
                <button
                    @click="rateFAQ(faq.id, 'yes')"
                    :class="{ active: faqFeedback[faq.id] === 'yes' }"
                >
                  <i class="far fa-thumbs-up"></i> Yes
                </button>
                <button
                    @click="rateFAQ(faq.id, 'no')"
                    :class="{ active: faqFeedback[faq.id] === 'no' }"
                >
                  <i class="far fa-thumbs-down"></i> No
                </button>
              </div>
            </div>
          </div>

          <div class="no-results" v-if="filteredFAQs.length === 0">
            <i class="far fa-frown"></i>
            <p>No relevant questions found</p>
            <button class="btn-contact" @click="scrollToSection('contact')">
              Contact Support
            </button>
          </div>
        </div>
      </div>
    </section>

    <!-- Contact Support -->
    <section class="contact-support" id="contact" ref="contactSection">
      <div class="container">
        <h2>Contact Support</h2>
        <div class="row">
          <div class="col-md-6">
            <form @submit.prevent="submitSupportRequest">
              <div class="form-group">
                <label for="support-subject">Issue Type</label>
                <select
                    id="support-subject"
                    class="form-control"
                    v-model="supportRequest.subject"
                    required
                >
                  <option value="" disabled selected>Select issue type</option>
                  <option
                      v-for="type in supportTypes"
                      :value="type.value"
                      :key="type.value"
                  >
                    {{ type.label }}
                  </option>
                </select>
              </div>

              <div class="form-group">
                <label for="support-name">Name</label>
                <input
                    type="text"
                    id="support-name"
                    class="form-control"
                    v-model="supportRequest.name"
                    required
                >
              </div>

              <div class="form-group">
                <label for="support-email">Email</label>
                <input
                    type="email"
                    id="support-email"
                    class="form-control"
                    v-model="supportRequest.email"
                    required
                >
              </div>

              <div class="form-group">
                <label for="support-order">Order Number (Optional)</label>
                <input
                    type="text"
                    id="support-order"
                    class="form-control"
                    v-model="supportRequest.orderNumber"
                >
              </div>

              <div class="form-group">
                <label for="support-message">Detailed Description</label>
                <textarea
                    id="support-message"
                    class="form-control"
                    rows="5"
                    v-model="supportRequest.message"
                    required
                ></textarea>
              </div>

              <div class="form-group">
                <label for="support-attachments">Add Attachments</label>
                <input
                    type="file"
                    id="support-attachments"
                    class="form-control"
                    multiple
                    @change="handleFileUpload"
                >
                <div class="file-preview" v-if="supportRequest.attachments.length > 0">
                  <div
                      class="file-item"
                      v-for="(file, index) in supportRequest.attachments"
                      :key="index"
                  >
                    <span>{{ file.name }}</span>
                    <button @click="removeAttachment(index)">
                      <i class="fas fa-times"></i>
                    </button>
                  </div>
                </div>
              </div>

              <button type="submit" class="btn-submit" :disabled="isSubmitting">
                <span v-if="!isSubmitting">Submit Request</span>
                <span v-else>Submitting...</span>
              </button>
            </form>
          </div>

          <div class="col-md-6">
            <div class="contact-methods">
              <div class="contact-card">
                <div class="icon-wrapper">
                  <i class="fas fa-phone-alt"></i>
                </div>
                <h3>Phone Support</h3>
                <p>24/7 Available</p>
                <a :href="`tel:${contactInfo.phone}`" class="contact-link">
                  {{ contactInfo.phone }}
                </a>
              </div>

              <div class="contact-card">
                <div class="icon-wrapper">
                  <i class="fas fa-comment-dots"></i>
                </div>
                <h3>Live Chat</h3>
                <p>Quick Response</p>
                <button class="contact-link" @click="openLiveChat">
                  Start Chat
                </button>
              </div>

              <div class="contact-card">
                <div class="icon-wrapper">
                  <i class="fas fa-envelope"></i>
                </div>
                <h3>Email</h3>
                <p>Response within 24 hours</p>
                <a :href="`mailto:${contactInfo.email}`" class="contact-link">
                  {{ contactInfo.email }}
                </a>
              </div>
            </div>

            <div class="response-time">
              <h4>Estimated Response Time</h4>
              <div class="time-item" v-for="item in responseTimes" :key="item.method">
                <span class="method">{{ item.method }}</span>
                <span class="time">{{ item.time }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Support Status Check -->
    <section class="status-check" v-if="hasOpenTickets">
      <div class="container">
        <div class="status-card">
          <h3>Check Your Support Request Status</h3>
          <p>Enter your request number to check the latest status</p>
          <div class="status-form">
            <input
                type="text"
                placeholder="Enter request number"
                v-model="ticketNumber"
            >
            <button @click="checkTicketStatus">
              <i class="fas fa-search"></i> Search
            </button>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue';

export default {
  name: 'Support',
  setup() {
    // Support options
    const supportOptions = ref([
      {
        id: 1,
        icon: 'far fa-question-circle',
        title: 'FAQs',
        description: 'Browse our frequently asked questions',
        target: 'faq'
      },
      {
        id: 2,
        icon: 'far fa-envelope',
        title: 'Contact Us',
        description: 'Reach us via email or form',
        target: 'contact'
      },
      {
        id: 3,
        icon: 'far fa-comments',
        title: 'Live Chat',
        description: 'Chat with our support team in real-time',
        target: 'chat'
      }
    ]);

    // FAQ data
    const faqs = ref([
      {
        id: 1,
        category: 'booking',
        question: 'How to book a travel product?',
        answer: 'You can select your desired travel product through our website or app, fill in the necessary information and complete the payment to finish the booking. After successful booking, you will receive a confirmation email.',
        helpful: true
      },
      {
        id: 2,
        category: 'payment',
        question: 'What payment methods are supported?',
        answer: 'We support various payment methods including credit cards (Visa, MasterCard), Alipay, WeChat Pay, UnionPay online payment, etc.',
        helpful: true
      },
      // More FAQs...
    ]);

    // FAQ categories
    const categories = ref([
      { id: 'all', name: 'All Questions' },
      { id: 'booking', name: 'Booking Issues' },
      { id: 'payment', name: 'Payment Issues' },
      { id: 'cancellation', name: 'Cancellation & Refund' },
      { id: 'account', name: 'Account Issues' }
    ]);

    // Contact support form
    const supportRequest = reactive({
      subject: '',
      name: '',
      email: '',
      orderNumber: '',
      message: '',
      attachments: []
    });

    // Support issue types
    const supportTypes = ref([
      { value: 'booking', label: 'Booking Issues' },
      { value: 'payment', label: 'Payment Issues' },
      { value: 'cancellation', label: 'Cancellation & Refund' },
      { value: 'account', label: 'Account Issues' },
      { value: 'technical', label: 'Technical Issues' },
      { value: 'other', label: 'Other Issues' }
    ]);

    // Contact information
    const contactInfo = reactive({
      phone: '400-123-4567',
      email: 'support@travelplatform.com',
      chatHours: '24/7'
    });

    // Response times
    const responseTimes = ref([
      { method: 'Phone Support', time: 'Immediately' },
      { method: 'Live Chat', time: 'Within 5 minutes' },
      { method: 'Email', time: 'Within 24 hours' },
      { method: 'Form Submission', time: 'Within 24 hours' }
    ]);

    // State variables
    const activeFAQ = ref(null);
    const activeCategory = ref('all');
    const searchQuery = ref('');
    const filteredFAQs = ref([]);
    const faqFeedback = reactive({});
    const isSubmitting = ref(false);
    const hasOpenTickets = ref(true);
    const ticketNumber = ref('');

    // Refs
    const faqSection = ref(null);
    const contactSection = ref(null);

    // Methods
    const toggleFAQ = (index) => {
      activeFAQ.value = activeFAQ.value === index ? null : index;
    };

    const filterByCategory = (categoryId) => {
      activeCategory.value = categoryId;
      filterFAQs();
    };

    const filterFAQs = () => {
      let results = [...faqs.value];

      // Filter by category
      if (activeCategory.value !== 'all') {
        results = results.filter(faq => faq.category === activeCategory.value);
      }

      // Filter by search query
      if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase();
        results = results.filter(faq =>
            faq.question.toLowerCase().includes(query) ||
            faq.answer.toLowerCase().includes(query)
        );
      }

      filteredFAQs.value = results;
    };

    const rateFAQ = (faqId, rating) => {
      faqFeedback[faqId] = rating;
      // Here you can add API call to record user feedback
    };

    const scrollToSection = (sectionId) => {
      if (sectionId === 'faq' && faqSection.value) {
        faqSection.value.scrollIntoView({ behavior: 'smooth' });
      } else if (sectionId === 'contact' && contactSection.value) {
        contactSection.value.scrollIntoView({ behavior: 'smooth' });
      } else if (sectionId === 'chat') {
        openLiveChat();
      }
    };

    const handleFileUpload = (event) => {
      const files = event.target.files;
      if (files) {
        for (let i = 0; i < files.length; i++) {
          supportRequest.attachments.push(files[i]);
        }
      }
    };

    const removeAttachment = (index) => {
      supportRequest.attachments.splice(index, 1);
    };

    const submitSupportRequest = () => {
      isSubmitting.value = true;

      // Simulate API call
      setTimeout(() => {
        console.log('Support request submitted:', supportRequest);
        isSubmitting.value = false;

        // Reset form
        Object.assign(supportRequest, {
          subject: '',
          name: '',
          email: '',
          orderNumber: '',
          message: '',
          attachments: []
        });

        alert('Your support request has been submitted successfully! Our team will contact you soon.');
      }, 1500);
    };

    const openLiveChat = () => {
      // Here you can integrate third-party chat tools
      alert('Live chat window will open soon');
    };

    const checkTicketStatus = () => {
      if (ticketNumber.value) {
        // Here you can add API call to check ticket status
        alert(`Checking status for ticket ${ticketNumber.value}...`);
      } else {
        alert('Please enter a valid request number');
      }
    };

    // Initialization
    onMounted(() => {
      filterFAQs();
    });

    return {
      supportOptions,
      faqs,
      categories,
      supportRequest,
      supportTypes,
      contactInfo,
      responseTimes,
      activeFAQ,
      activeCategory,
      searchQuery,
      filteredFAQs,
      faqFeedback,
      isSubmitting,
      hasOpenTickets,
      ticketNumber,
      faqSection,
      contactSection,
      toggleFAQ,
      filterByCategory,
      filterFAQs,
      rateFAQ,
      scrollToSection,
      handleFileUpload,
      removeAttachment,
      submitSupportRequest,
      openLiveChat,
      checkTicketStatus
    };
  }
};
</script>
<style scoped lang="scss">
.support-page {
  font-family: 'Arial', sans-serif;
  color: #333;

  .support-hero {
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
      opacity: 0.9;
    }
  }

  .support-options {
    padding: 60px 0;
    background: #f8f9fa;

    .support-card {
      background: white;
      border-radius: 8px;
      padding: 30px;
      height: 100%;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
      transition: transform 0.3s, box-shadow 0.3s;
      cursor: pointer;

      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
      }

      .icon-wrapper {
        width: 60px;
        height: 60px;
        background: #e3f2fd;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 20px;

        i {
          font-size: 1.5rem;
          color: #2196f3;
        }
      }

      h3 {
        font-size: 1.3rem;
        margin-bottom: 15px;
      }

      p {
        color: #666;
        margin-bottom: 20px;
      }

      .link-arrow {
        color: #2196f3;
        font-weight: 500;
        text-decoration: none;
        display: inline-flex;
        align-items: center;

        i {
          margin-left: 5px;
          font-size: 0.8rem;
          transition: transform 0.3s;
        }

        &:hover i {
          transform: translateX(3px);
        }
      }
    }
  }

  .faq-section {
    padding: 80px 0;

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 30px;
      flex-wrap: wrap;

      h2 {
        font-size: 2rem;
        font-weight: bold;
        margin-bottom: 20px;
      }

      .search-box {
        position: relative;
        width: 300px;
        max-width: 100%;

        input {
          width: 100%;
          padding: 10px 15px 10px 40px;
          border: 1px solid #ddd;
          border-radius: 30px;
          font-size: 1rem;
          transition: border-color 0.3s;

          &:focus {
            border-color: #2196f3;
            outline: none;
          }
        }

        i {
          position: absolute;
          left: 15px;
          top: 50%;
          transform: translateY(-50%);
          color: #999;
        }
      }
    }

    .faq-categories {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
      margin-bottom: 30px;

      button {
        padding: 8px 16px;
        background: #f1f1f1;
        border: none;
        border-radius: 20px;
        font-size: 0.9rem;
        cursor: pointer;
        transition: background 0.3s, color 0.3s;

        &.active {
          background: #2196f3;
          color: white;
        }

        &:hover:not(.active) {
          background: #e0e0e0;
        }
      }
    }

    .faq-accordion {
      .faq-item {
        border: 1px solid #eee;
        border-radius: 8px;
        margin-bottom: 15px;
        overflow: hidden;
        transition: box-shadow 0.3s;

        &:hover {
          box-shadow: 0 3px 10px rgba(0, 0, 0, 0.05);
        }

        &.active {
          border-color: #2196f3;
        }

        .faq-question {
          padding: 20px;
          display: flex;
          justify-content: space-between;
          align-items: center;
          cursor: pointer;
          background: #fafafa;

          h3 {
            font-size: 1.1rem;
            font-weight: 600;
            margin: 0;
          }

          i {
            color: #2196f3;
            font-size: 1rem;
          }
        }

        .faq-answer {
          padding: 0 20px;
          max-height: 0;
          overflow: hidden;
          transition: max-height 0.3s ease-out, padding 0.3s ease;

          &.show {
            padding: 0 20px 20px;
            max-height: 500px;
          }

          p {
            margin-bottom: 15px;
            line-height: 1.6;
          }

          .faq-helpful {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-top: 15px;
            padding-top: 15px;
            border-top: 1px dashed #eee;

            span {
              font-size: 0.9rem;
              color: #666;
            }

            button {
              background: none;
              border: 1px solid #ddd;
              border-radius: 4px;
              padding: 3px 8px;
              font-size: 0.8rem;
              cursor: pointer;
              display: flex;
              align-items: center;
              gap: 5px;

              &.active {
                background: #e3f2fd;
                border-color: #2196f3;
                color: #2196f3;
              }

              &:hover:not(.active) {
                background: #f5f5f5;
              }
            }
          }
        }
      }

      .no-results {
        text-align: center;
        padding: 40px 0;

        i {
          font-size: 3rem;
          color: #999;
          margin-bottom: 20px;
        }

        p {
          font-size: 1.2rem;
          margin-bottom: 20px;
        }

        .btn-contact {
          background: #2196f3;
          color: white;
          border: none;
          padding: 10px 20px;
          border-radius: 4px;
          font-weight: 500;
          cursor: pointer;
          transition: background 0.3s;

          &:hover {
            background: #0d8bf2;
          }
        }
      }
    }
  }

  .contact-support {
    padding: 80px 0;
    background: #f8f9fa;

    h2 {
      font-size: 2rem;
      font-weight: bold;
      margin-bottom: 40px;
      text-align: center;
    }

    form {
      background: white;
      padding: 30px;
      border-radius: 8px;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);

      .form-group {
        margin-bottom: 20px;

        label {
          display: block;
          margin-bottom: 8px;
          font-weight: 500;
        }

        .form-control {
          width: 100%;
          padding: 10px 15px;
          border: 1px solid #ddd;
          border-radius: 4px;
          font-size: 1rem;
          transition: border-color 0.3s;

          &:focus {
            border-color: #2196f3;
            outline: none;
          }
        }

        textarea.form-control {
          resize: vertical;
          min-height: 120px;
        }

        .file-preview {
          margin-top: 10px;

          .file-item {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 8px 10px;
            background: #f5f5f5;
            border-radius: 4px;
            margin-bottom: 5px;

            span {
              font-size: 0.9rem;
              white-space: nowrap;
              overflow: hidden;
              text-overflow: ellipsis;
              max-width: 80%;
            }

            button {
              background: none;
              border: none;
              color: #999;
              cursor: pointer;
              padding: 0 5px;

              &:hover {
                color: #f44336;
              }
            }
          }
        }
      }

      .btn-submit {
        background: #2196f3;
        color: white;
        border: none;
        padding: 12px 25px;
        border-radius: 4px;
        font-size: 1rem;
        font-weight: 500;
        cursor: pointer;
        transition: background 0.3s;
        width: 100%;

        &:hover:not(:disabled) {
          background: #0d8bf2;
        }

        &:disabled {
          background: #90caf9;
          cursor: not-allowed;
        }
      }
    }

    .contact-methods {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 20px;
      margin-bottom: 30px;

      .contact-card {
        background: white;
        padding: 25px;
        border-radius: 8px;
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
        text-align: center;

        .icon-wrapper {
          width: 60px;
          height: 60px;
          background: #e3f2fd;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          margin: 0 auto 20px;

          i {
            font-size: 1.5rem;
            color: #2196f3;
          }
        }

        h3 {
          font-size: 1.2rem;
          margin-bottom: 10px;
        }

        p {
          color: #666;
          font-size: 0.9rem;
          margin-bottom: 15px;
        }

        .contact-link {
          display: inline-block;
          color: #2196f3;
          font-weight: 500;
          text-decoration: none;
          border: 1px solid #2196f3;
          padding: 6px 15px;
          border-radius: 20px;
          font-size: 0.9rem;
          transition: background 0.3s, color 0.3s;

          &:hover {
            background: #2196f3;
            color: white;
          }
        }
      }
    }

    .response-time {
      background: white;
      padding: 25px;
      border-radius: 8px;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);

      h4 {
        font-size: 1.2rem;
        margin-bottom: 20px;
        padding-bottom: 10px;
        border-bottom: 1px solid #eee;
      }

      .time-item {
        display: flex;
        justify-content: space-between;
        margin-bottom: 12px;

        .method {
          color: #666;
        }

        .time {
          font-weight: 500;
        }
      }
    }
  }

  .status-check {
    padding: 40px 0;

    .status-card {
      background: linear-gradient(135deg, #2196f3 0%, #0d47a1 100%);
      color: white;
      padding: 30px;
      border-radius: 8px;
      text-align: center;

      h3 {
        font-size: 1.5rem;
        margin-bottom: 10px;
      }

      p {
        margin-bottom: 20px;
        opacity: 0.9;
      }

      .status-form {
        display: flex;
        max-width: 500px;
        margin: 0 auto;

        input {
          flex: 1;
          padding: 12px 15px;
          border: none;
          border-radius: 4px 0 0 4px;
          font-size: 1rem;

          &:focus {
            outline: none;
          }
        }

        button {
          background: #ff9800;
          color: white;
          border: none;
          padding: 0 20px;
          border-radius: 0 4px 4px 0;
          font-weight: 500;
          cursor: pointer;
          transition: background 0.3s;

          &:hover {
            background: #f57c00;
          }
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .support-page {
    .support-hero {
      padding: 80px 0;

      h1 {
        font-size: 2rem;
      }

      p {
        font-size: 1.2rem;
      }
    }

    .faq-section {
      padding: 50px 0;

      .section-header {
        flex-direction: column;
        align-items: flex-start;

        .search-box {
          width: 100%;
          margin-top: 15px;
        }
      }
    }

    .contact-support {
      padding: 50px 0;

      .contact-methods {
        grid-template-columns: 1fr;
      }
    }
  }
}
</style>