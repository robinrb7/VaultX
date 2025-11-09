# VaultX

# 🏦 Full-Stack Banking Management System  
### Secure Digital Banking Platform built with Kotlin, Spring Boot, MySQL, and Android

---

## 📘 Overview

The **VaultX** is a full-stack digital banking platform that enables users to:
- Register and manage multiple types of bank accounts
- Perform secure fund transfers with OTP and PIN verification
- Track transaction history and balances
- Access real-time account details via web or mobile frontend

This project aims to simulate **real-world digital banking workflows** with modern security practices — JWT-based authentication, encrypted PINs, OTP verification, and dynamic account operations.

---

## ✨ Features

### 👤 **Authentication & Authorization**
- JWT-based secure login and registration  
- Refresh token support for seamless session renewal  
- Passwords hashed using BCrypt  
- Role-based access control with Spring Security  

### 💳 **Account Management**
- Create and manage multiple account types:
  - 🏦 Savings Account (Flexi-enabled)
  - 🧾 Current Account
  - 💰 Fixed Deposit Account  
- Auto-generated IFSC codes  
- Retrieve and delete user accounts (ownership verified)  

### 💸 **Transactions**
- Transfer funds between accounts  
- Real-time debit/credit balance updates  
- Complete transaction logs with timestamps and remarks  
- View detailed transaction history for each account  
- Ownership check — users can only transfer from their own accounts  

### 🔐 **Security Layer**
- 4-digit Transaction PIN setup & verification  
- Auto account lock after 3 failed PIN attempts  
- OTP-based 2-step verification for sensitive actions (e.g., fund transfers)  
- OTP delivery via:
  - ✉️ Email (Spring Boot Mail)
  - 📱 SMS (Twilio API)  

### 💼 **Flexi Savings**
- Enable/disable Flexi mode  
- User-defined threshold (₹10,000 - ₹1,00,000)  
- Plans to auto-convert surplus funds into short-term FDs  

### 📱 **Frontend (Android)**
- Android app built with **Jetpack Compose**  
- Secure API communication with JWT tokens    
- Modern UI with real-time balance display and transaction tracking  

---

## 🧩 Tech Stack

| Layer | Technology |
|--------|-------------|
| **Frontend** | Android (Kotlin + Jetpack Compose) |
| **Backend** | Spring Boot (Kotlin) |
| **Database** | MySQL |
| **ORM** | Hibernate / JPA |
| **Authentication** | Spring Security + JWT |
| **OTP Delivery** | Twilio SMS + Gmail SMTP |
| **Build Tools** | Gradle (Backend) / Android Gradle (Frontend) |
| **Version Control** | Git + GitHub |

---

## 🏗️ System Architecture

```plaintext
┌───────────────────────────────────────────────────────────────┐
│                         FRONTEND                              │
│───────────────────────────────────────────────────────────────│
│ Android App (Kotlin + Jetpack Compose)                        │
│                                                               │
│ → Communicates via REST APIs (JSON)                           │
└───────────────────────────────────────────────────────────────┘
                │
                ▼
┌───────────────────────────────────────────────────────────────┐
│                         BACKEND (Spring Boot)                 │
│───────────────────────────────────────────────────────────────│
│ Controllers → Services → Repositories → MySQL DB              │
│ Security: JWT + PIN + OTP                                     │
│ Business Logic: Accounts, Transactions, Flexi Savings         │
└───────────────────────────────────────────────────────────────┘
                │
                ▼
┌───────────────────────────────────────────────────────────────┐
│                         DATABASE (MySQL)                      │
│───────────────────────────────────────────────────────────────│
│ customers, accounts, transactions, otp_verifications,          │
│ account_security, fixed_deposit_account, etc.                  │
└───────────────────────────────────────────────────────────────┘
