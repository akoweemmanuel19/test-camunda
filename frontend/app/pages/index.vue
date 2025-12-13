<template>
  <div class="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900">
    <div class="absolute inset-0 bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjAiIGhlaWdodD0iNjAiIHZpZXdCb3g9IjAgMCA2MCA2MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48ZyBmaWxsPSJub25lIiBmaWxsLXJ1bGU9ImV2ZW5vZGQiPjxnIGZpbGw9IiM4YjViZjYiIGZpbGwtb3BhY2l0eT0iMC4wNSI+PHBhdGggZD0iTTM2IDEzNGgtMnYtMmgydjJ6bS01IDVoLTJ2LTJoMnYyem01IDBoLTJ2LTJoMnYyem0tNSA1aC0ydi0yaDJ2MnptNSAwaC0ydi0yaDJ2MnoiLz48L2c+PC9nPjwvc3ZnPg==')] opacity-20"></div>
    
    <Navbar />

    <div class="relative container mx-auto p-4 sm:p-6 lg:p-8">

      <!-- Not Logged In State -->
      <div v-if="!isLoggedIn" class="max-w-2xl mx-auto mt-20">
        <div class="relative">
          <div class="absolute -inset-1 bg-gradient-to-r from-purple-600 to-pink-600 rounded-2xl blur-xl opacity-75 animate-pulse"></div>
          <div class="relative bg-slate-800 bg-opacity-90 backdrop-blur-xl rounded-2xl shadow-2xl p-12 text-center border border-slate-700">
            <div class="w-20 h-20 bg-gradient-to-br from-purple-500 to-pink-500 rounded-2xl mx-auto mb-6 flex items-center justify-center">
              <svg class="w-10 h-10 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
              </svg>
            </div>
            <h2 class="text-3xl font-bold text-white mb-4">Accès Restreint</h2>
            <p class="text-slate-400 mb-8 leading-relaxed">
              Veuillez vous connecter pour accéder à votre espace de gestion des workflows et demandes de congés.
            </p>
            <NuxtLink 
              to="/login"
              class="inline-flex items-center space-x-2 px-8 py-4 bg-gradient-to-r from-purple-600 to-pink-600 hover:from-purple-500 hover:to-pink-500 text-white font-semibold rounded-xl transition-all duration-300 transform hover:scale-105 shadow-lg"
            >
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 16l-4-4m0 0l4-4m-4 4h14m-5 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h7a3 3 0 013 3v1" />
              </svg>
              <span>Se connecter</span>
            </NuxtLink>
          </div>
        </div>
      </div>

      <!-- Logged In State -->
      <div v-else class="space-y-8 mt-8">

        <!-- Welcome Card -->
        <div class="relative overflow-hidden bg-gradient-to-br from-purple-900 to-indigo-900 rounded-2xl shadow-2xl p-8 border border-purple-700">
          <div class="absolute top-0 right-0 w-96 h-96 bg-purple-500 rounded-full filter blur-3xl opacity-20 -mr-48 -mt-48"></div>
          
          <div class="relative flex items-center justify-between flex-wrap gap-4">
            <div class="flex items-center space-x-6">
              <div class="w-20 h-20 bg-gradient-to-br from-purple-500 to-pink-500 rounded-2xl flex items-center justify-center text-3xl font-bold text-white shadow-2xl transform hover:rotate-12 transition-transform duration-300">
                {{ user.personne.prenom.charAt(0) }}
              </div>
              <div>
                <h1 class="text-4xl font-bold text-white mb-2">
                  Bienvenue, {{ user.personne.prenom }} 👋
                </h1>
                <div class="flex items-center space-x-4 text-slate-300">
                  <span :class="getPosteGradient(user.personne.poste)" class="px-4 py-2 text-sm font-bold rounded-full bg-opacity-20 backdrop-blur-sm border border-white border-opacity-30">
                    {{ getRoleEmoji(user.personne.poste) }} {{ user.personne.poste }}
                  </span>
                  <span class="px-3 py-1 text-xs font-mono bg-slate-800 bg-opacity-50 rounded-lg">
                    ID: {{ user.personne.id }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Dashboard Content Based on Role -->
        <div class="grid grid-cols-1">

          <!-- Employee Dashboard -->
          <template v-if="user.personne.poste === 'DEVELOPPEUR' || user.personne.poste === 'ANALYSTE'">
            <div class="relative overflow-hidden bg-slate-800 bg-opacity-50 backdrop-blur-xl rounded-2xl shadow-2xl border border-slate-700">
              <div class="absolute top-0 left-0 w-full h-1 bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500"></div>
              <div class="p-6 border-b border-slate-700">
                <div class="flex items-center space-x-3">
                  <div class="w-10 h-10 bg-indigo-500 bg-opacity-30 rounded-lg flex items-center justify-center">
                    <svg class="w-6 h-6 text-indigo-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                    </svg>
                  </div>
                  <div>
                    <h2 class="text-2xl font-bold text-white">Espace Employé</h2>
                    <p class="text-sm text-slate-400">Gérez vos demandes de congés</p>
                  </div>
                </div>
              </div>
              <div class="p-6">
                <EmployeeDashbaord />
              </div>
            </div>
          </template>

          <!-- Manager Dashboard -->
          <template v-if="user.personne.poste === 'MANAGER'">
            <div class="relative overflow-hidden bg-slate-800 bg-opacity-50 backdrop-blur-xl rounded-2xl shadow-2xl border border-slate-700">
              <div class="absolute top-0 left-0 w-full h-1 bg-gradient-to-r from-teal-500 via-cyan-500 to-blue-500"></div>
              <div class="p-6 border-b border-slate-700">
                <div class="flex items-center space-x-3">
                  <div class="w-10 h-10 bg-teal-500 bg-opacity-30 rounded-lg flex items-center justify-center">
                    <svg class="w-6 h-6 text-teal-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4" />
                    </svg>
                  </div>
                  <div>
                    <h2 class="text-2xl font-bold text-white">Espace Manager</h2>
                    <p class="text-sm text-slate-400">Approuvez ou rejetez les demandes</p>
                  </div>
                </div>
              </div>
              <div class="p-6">
                <ManagerDashboard />
              </div>
            </div>
          </template>

          <!-- Admin Dashboard -->
          <template v-if="user.personne.poste === 'ADMIN'">
            <div class="relative overflow-hidden bg-slate-800 bg-opacity-50 backdrop-blur-xl rounded-2xl shadow-2xl border border-slate-700">
              <div class="absolute top-0 left-0 w-full h-1 bg-gradient-to-r from-red-500 via-pink-500 to-rose-500"></div>
              <div class="p-6 border-b border-slate-700">
                <div class="flex items-center space-x-3">
                  <div class="w-10 h-10 bg-red-500 bg-opacity-30 rounded-lg flex items-center justify-center">
                    <svg class="w-6 h-6 text-red-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                    </svg>
                  </div>
                  <div>
                    <h2 class="text-2xl font-bold text-white">Espace Administration</h2>
                    <p class="text-sm text-slate-400">Accès complet au système</p>
                  </div>
                </div>
              </div>
              <div class="p-6">
                <AdminDashboard />
              </div>
            </div>
          </template>

        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAuth } from '~/composables/useAuth';
import Navbar from '~/components/Navbar.vue';
import EmployeeDashbaord from '~/components/EmployeeDashbaord.vue';
import ManagerDashboard from '~/components/ManagerDashboard.vue';
import AdminDashboard from '~/components/AdminDashboard.vue';

const { user, isLoggedIn } = useAuth();

const getPosteGradient = (poste) => {
  const gradients = {
    'DEVELOPPEUR': 'bg-gradient-to-r from-indigo-500 to-purple-500 text-white',
    'ANALYSTE': 'bg-gradient-to-r from-cyan-500 to-blue-500 text-white',
    'MANAGER': 'bg-gradient-to-r from-teal-500 to-emerald-500 text-white',
    'ADMIN': 'bg-gradient-to-r from-red-500 to-pink-500 text-white'
  };
  return gradients[poste] || 'bg-gray-500 text-white';
};

const getRoleEmoji = (poste) => {
  const emojis = {
    'DEVELOPPEUR': '👨‍💻',
    'ANALYSTE': '📊',
    'MANAGER': '👔',
    'ADMIN': '⚙️'
  };
  return emojis[poste] || '👤';
};
</script>