<template>
  <nav class="bg-gradient-to-r from-slate-900 via-purple-900 to-slate-900 text-white shadow-2xl sticky top-0 z-50 backdrop-blur-sm bg-opacity-95">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between items-center h-16">
        
        <NuxtLink to="/" class="flex items-center space-x-3 group">
          <div class="w-10 h-10 bg-gradient-to-br from-purple-500 to-pink-500 rounded-lg flex items-center justify-center transform group-hover:rotate-12 transition-transform duration-300">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
          </div>
          <span class="text-xl font-bold bg-gradient-to-r from-purple-400 to-pink-400 bg-clip-text text-transparent">
            Camunda Test App
          </span>
        </NuxtLink>
        
        <div v-if="isLoggedIn" class="flex items-center space-x-4">
          <div class="flex items-center space-x-3 bg-slate-800 bg-opacity-50 px-4 py-2 rounded-full backdrop-blur-sm">
            <div class="w-8 h-8 bg-gradient-to-br from-purple-500 to-pink-500 rounded-full flex items-center justify-center text-xs font-bold shadow-lg">
              {{ user.personne.prenom.charAt(0) }}
            </div>
            <span class="text-sm font-medium">{{ user.personne.prenom }}</span>
            <span class="px-2 py-1 text-xs font-semibold rounded-full bg-purple-500 bg-opacity-30 text-purple-300">
              {{ user.personne.poste }}
            </span>
          </div>
          <button 
            @click="handleLogout()" 
            class="group flex items-center space-x-2 px-4 py-2 text-sm font-medium bg-slate-800 bg-opacity-50 hover:bg-red-600 rounded-lg transition-all duration-300 transform hover:scale-105"
          >
            <svg class="w-4 h-4 group-hover:rotate-180 transition-transform duration-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
            </svg>
            <span>Déconnexion</span>
          </button>
        </div>
        
        <div v-else>
          <NuxtLink 
            to="/login" 
            class="flex items-center space-x-2 px-4 py-2 text-sm font-medium bg-gradient-to-r from-purple-600 to-pink-600 hover:from-purple-500 hover:to-pink-500 rounded-lg transition-all duration-300 transform hover:scale-105 shadow-lg"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 16l-4-4m0 0l4-4m-4 4h14m-5 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h7a3 3 0 013 3v1" />
            </svg>
            <span>Connexion</span>
          </NuxtLink>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { useAuth } from '~/composables/useAuth';
import { useRouter } from 'vue-router';

const { user, isLoggedIn, logout } = useAuth();
const router = useRouter();

const handleLogout = () => {
  logout();
  router.push('/login');
}
</script>