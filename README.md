
# NutriTrack 

A personalized nutrition insights platform built for Android, featuring **Room database integration**, **user authentication**, **GenAI-powered recommendations**, and **real-time dietary analytics**.

---

## Overview
NutriTrack helps users track dietary habits, receive AI-driven tips for better nutrition, and access personalized insights.  
Clinicians and admins can view aggregated statistics and AI-generated trends from user data.

---

## Key Features
- **Local Database (Room)**
  - Stores user data, dietary intake, and AI tips.
  - CSV data imported only on first launch.
- **Secure Authentication**
  - Account claim on first login.
  - Persistent login sessions with logout functionality.
- **NutriCoach AI Assistant**
  - Fetches fruit facts via [FruityVice API](https://www.fruityvice.com/).
  - Uses GenAI (Google Gemini) to generate motivational dietary tips.
  - Stores AI suggestions for historical viewing.
- **Admin Dashboard**
  - Displays gender-based HEIFA score averages.
  - Uses AI to highlight patterns in user data.
- **Modern Android Architecture**
  - MVVM with LiveData for reactive UI.
  - Retrofit + Coroutines for networking.

---

## Tech Stack
- **Language:** Kotlin  
- **Architecture:** MVVM (Model-View-ViewModel)  
- **Database:** Room (SQLite)  
- **Networking:** Retrofit & Coroutines  
- **AI Integration:** Google Gemini API  
- **UI:** Material Design, LiveData for real-time updates  

---

## Setup & Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/NutriTrack-.git
