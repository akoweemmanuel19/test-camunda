<template>
  <div class="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900 flex items-center justify-center p-4">
    <div class="absolute inset-0 bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjAiIGhlaWdodD0iNjAiIHZpZXdCb3g9IjAgMCA2MCA2MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48ZyBmaWxsPSJub25lIiBmaWxsLXJ1bGU9ImV2ZW5vZGQiPjxnIGZpbGw9IiM4YjViZjYiIGZpbGwtb3BhY2l0eT0iMC4wNSI+PHBhdGggZD0iTTM2IDEzNGgtMnYtMmgydjJ6bS01IDVoLTJ2LTJoMnYyem01IDBoLTJ2LTJoMnYyem0tNSA1aC0ydi0yaDJ2MnptNSAwaC0ydi0yaDJ2MnoiLz48L2c+PC9nPjwvc3ZnPg==')] opacity-20"></div>
    
    <div class="w-full max-w-md relative">
      <!-- Glow effect -->
      <div class="absolute -inset-1 bg-gradient-to-r from-purple-600 to-pink-600 rounded-2xl blur-xl opacity-75 animate-pulse"></div>
      
      <div class="relative bg-slate-800 bg-opacity-90 backdrop-blur-xl rounded-2xl shadow-2xl p-8 border border-slate-700">
        
        <!-- Logo & Title -->
        <div class="text-center mb-8">
          <div class="w-16 h-16 bg-gradient-to-br from-purple-500 to-pink-500 rounded-2xl mx-auto mb-4 flex items-center justify-center transform hover:rotate-12 transition-transform duration-300">
            <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
          </div>
          <h2 class="text-3xl font-bold bg-gradient-to-r from-purple-400 to-pink-400 bg-clip-text text-transparent mb-2">
            Bienvenue
          </h2>
          <p class="text-slate-400 text-sm">Connectez-vous à votre espace de travail</p>
        </div>

        <!-- Form -->
        <form @submit.prevent="handleLogin" class="space-y-6">
          
          <!-- Identifiant Input -->
          <div class="space-y-2">
            <label class="text-sm font-medium text-slate-300 flex items-center space-x-2">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
              <span>Identifiant</span>
            </label>
            <input 
              v-model="identifiant" 
              type="text" 
              placeholder="alice_d" 
              required
              class="w-full px-4 py-3 bg-slate-700 bg-opacity-50 border border-slate-600 rounded-lg text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent transition-all duration-300"
            >
          </div>

          <!-- Password Input -->
          <div class="space-y-2">
            <label class="text-sm font-medium text-slate-300 flex items-center space-x-2">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
              </svg>
              <span>Mot de passe</span>
            </label>
            <input 
              v-model="motDePasse" 
              type="password" 
              placeholder="••••••••" 
              required
              class="w-full px-4 py-3 bg-slate-700 bg-opacity-50 border border-slate-600 rounded-lg text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent transition-all duration-300"
            >
          </div>

          <!-- Error Message -->
          <div v-if="error" class="p-4 bg-red-500 bg-opacity-10 border border-red-500 rounded-lg flex items-start space-x-3">
            <svg class="w-5 h-5 text-red-400 flex-shrink-0 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <p class="text-sm text-red-300">{{ error }}</p>
          </div>

          <!-- Submit Button -->
          <button 
            type="submit" 
            :disabled="loading"
            class="w-full py-3 px-4 bg-gradient-to-r from-purple-600 to-pink-600 hover:from-purple-500 hover:to-pink-500 text-white font-semibold rounded-lg transition-all duration-300 transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none shadow-lg flex items-center justify-center space-x-2"
          >
            <svg v-if="loading" class="animate-spin h-5 w-5" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            <span>{{ loading ? 'Connexion en cours...' : 'Se connecter' }}</span>
          </button>
        </form>

        <!-- Footer -->
        <div class="mt-6 text-center text-slate-400 text-xs">
          <p>Exemples: alice_d / pass, bob_a / pass, charlie_m / pass</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useAuth } from '~/composables/useAuth';
import { useRouter } from 'vue-router';

const identifiant = ref('');
const motDePasse = ref('');
const loading = ref(false);
const error = ref(null);
const { login } = useAuth();
const router = useRouter();

const handleLogin = async () => {
  loading.value = true;
  error.value = null;

  try {
    const response = await fetch('http://localhost:8080/api/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ 
        identifiant: identifiant.value, 
        motDePasse: motDePasse.value 
      }),
    });

    if (!response.ok) {
      throw new Error("Identifiants incorrects. Veuillez réessayer.");
    }

    const userData = await response.json();
    login(userData);
    router.push('/');

  } catch (err) {
    error.value = err.message || 'Erreur de connexion au serveur.';
  } finally {
    loading.value = false;
  }
};
</script>