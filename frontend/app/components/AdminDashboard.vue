<template>
  <div class="space-y-6">

    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      
      <div class="relative overflow-hidden bg-gradient-to-br from-blue-900 to-indigo-900 rounded-2xl shadow-2xl p-6 border border-blue-700 transform hover:scale-105 transition-transform duration-300">
        <div class="absolute top-0 right-0 w-32 h-32 bg-blue-500 rounded-full filter blur-3xl opacity-20 -mr-16 -mt-16"></div>
        <div class="relative">
          <div class="flex items-center justify-between mb-2">
            <p class="text-sm font-medium text-blue-300">Utilisateurs Totaux</p>
            <div class="w-10 h-10 bg-blue-500 bg-opacity-30 rounded-lg flex items-center justify-center">
              <svg class="w-5 h-5 text-blue-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
              </svg>
            </div>
          </div>
          <p class="text-4xl font-bold text-white">{{ stats.totalUsers }}</p>
        </div>
      </div>

      <div class="relative overflow-hidden bg-gradient-to-br from-yellow-900 to-orange-900 rounded-2xl shadow-2xl p-6 border border-yellow-700 transform hover:scale-105 transition-transform duration-300">
        <div class="absolute top-0 right-0 w-32 h-32 bg-yellow-500 rounded-full filter blur-3xl opacity-20 -mr-16 -mt-16"></div>
        <div class="relative">
          <div class="flex items-center justify-between mb-2">
            <p class="text-sm font-medium text-yellow-300">Demandes en Attente</p>
            <div class="w-10 h-10 bg-yellow-500 bg-opacity-30 rounded-lg flex items-center justify-center">
              <svg class="w-5 h-5 text-yellow-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
          </div>
          <p class="text-4xl font-bold text-white">{{ stats.pendingRequests }}</p>
        </div>
      </div>

      <div class="relative overflow-hidden bg-gradient-to-br from-green-900 to-emerald-900 rounded-2xl shadow-2xl p-6 border border-green-700 transform hover:scale-105 transition-transform duration-300">
        <div class="absolute top-0 right-0 w-32 h-32 bg-green-500 rounded-full filter blur-3xl opacity-20 -mr-16 -mt-16"></div>
        <div class="relative">
          <div class="flex items-center justify-between mb-2">
            <p class="text-sm font-medium text-green-300">Processus Actifs</p>
            <div class="w-10 h-10 bg-green-500 bg-opacity-30 rounded-lg flex items-center justify-center">
              <svg class="w-5 h-5 text-green-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
              </svg>
            </div>
          </div>
          <p class="text-4xl font-bold text-white">{{ stats.activeProcesses }}</p>
        </div>
      </div>
      
    </div>

    <!-- Users Management Table -->
    <div class="bg-slate-800 bg-opacity-50 backdrop-blur-xl rounded-2xl shadow-2xl border border-slate-700 overflow-hidden">
      
      <div class="flex justify-between items-center p-6 border-b border-slate-700 bg-gradient-to-r from-red-900 to-pink-900 bg-opacity-30">
        <div class="flex items-center space-x-3">
          <div class="w-10 h-10 bg-red-500 bg-opacity-30 rounded-lg flex items-center justify-center">
            <svg class="w-6 h-6 text-red-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
          </div>
          <div>
            <h3 class="text-xl font-bold text-white">Gestion des Utilisateurs</h3>
            <p class="text-sm text-slate-400">Administrer les postes et accès</p>
          </div>
        </div>
        <button 
          @click="fetchUsers" 
          :disabled="loadingUsers"
          class="flex items-center space-x-2 px-5 py-3 bg-gradient-to-r from-red-600 to-pink-600 hover:from-red-500 hover:to-pink-500 text-white font-semibold rounded-xl transition-all duration-300 transform hover:scale-105 disabled:opacity-50 shadow-lg"
        >
          <svg class="w-5 h-5" :class="{ 'animate-spin': loadingUsers }" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
          </svg>
          <span>{{ loadingUsers ? 'Chargement...' : 'Actualiser' }}</span>
        </button>
      </div>

      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-slate-700">
          <thead class="bg-slate-900 bg-opacity-50">
            <tr>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-300 uppercase tracking-wider">ID</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-300 uppercase tracking-wider">Utilisateur</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-300 uppercase tracking-wider">Identifiant</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-300 uppercase tracking-wider">Poste</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-300 uppercase tracking-wider">Actions</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-700">
            <tr v-for="user in users" :key="user.id" class="hover:bg-slate-700 hover:bg-opacity-30 transition-colors">
              <td class="px-6 py-4 whitespace-nowrap text-sm font-mono text-purple-400">#{{ user.id }}</td>
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="flex items-center space-x-3">
                  <div class="w-10 h-10 bg-gradient-to-br from-red-500 to-pink-500 rounded-full flex items-center justify-center text-sm font-bold text-white shadow-lg">
                    {{ user.personne.prenom.charAt(0) }}{{ user.personne.nom.charAt(0) }}
                  </div>
                  <div>
                    <div class="text-sm font-medium text-slate-300">{{ user.personne.prenom }} {{ user.personne.nom }}</div>
                    <div class="text-xs text-slate-500">ID Personne: {{ user.personne.id }}</div>
                  </div>
                </div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-400 font-mono">{{ user.identifiant }}</td>
              
              <td class="px-6 py-4 whitespace-nowrap">
                <span :class="getPosteClass(user.personne.poste)" class="px-3 py-1 inline-flex text-xs leading-5 font-bold rounded-full">
                  {{ user.personne.poste }}
                </span>
              </td>

              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium flex space-x-2">
                <button 
                  @click="openEditModal(user)" 
                  class="p-2 text-indigo-400 hover:text-indigo-300 hover:bg-indigo-500 hover:bg-opacity-20 rounded-lg transition-all"
                  title="Modifier le poste"
                >
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                  </svg>
                </button>
                <button 
                  @click="deleteUser(user.id)" 
                  class="p-2 text-red-400 hover:text-red-300 hover:bg-red-500 hover:bg-opacity-20 rounded-lg transition-all"
                  title="Supprimer l'utilisateur"
                >
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                  </svg>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <p v-if="!users.length && !loadingUsers" class="p-8 text-center text-slate-400">
        <svg class="w-12 h-12 mx-auto mb-3 text-slate-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
        </svg>
        Aucun utilisateur trouvé.
      </p>
    </div>

    <!-- Edit Modal -->
    <div v-if="isModalOpen" class="fixed inset-0 bg-black bg-opacity-75 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <div class="relative w-full max-w-md">
        <div class="absolute -inset-1 bg-gradient-to-r from-purple-600 to-pink-600 rounded-2xl blur opacity-75"></div>
        <div class="relative bg-slate-800 rounded-2xl shadow-2xl p-8 border border-slate-700">
          <h4 class="text-2xl font-bold text-white mb-6">Modifier le Poste</h4>
          <div class="space-y-4">
            <div class="flex items-center space-x-3 p-3 bg-slate-700 bg-opacity-50 rounded-lg">
              <div class="w-10 h-10 bg-gradient-to-br from-purple-500 to-pink-500 rounded-full flex items-center justify-center text-sm font-bold text-white">
                {{ userToEdit.personne.prenom.charAt(0) }}
              </div>
              <div>
                <div class="text-sm font-medium text-slate-300">{{ userToEdit.personne.prenom }} {{ userToEdit.personne.nom }}</div>
                <div class="text-xs text-slate-500">{{ userToEdit.identifiant }}</div>
              </div>
            </div>
            
            <div class="space-y-2">
              <label class="text-sm font-medium text-slate-300">Nouveau Poste</label>
              <select 
                v-model="newPoste" 
                class="w-full px-4 py-3 bg-slate-700 bg-opacity-50 border border-slate-600 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-purple-500 transition-all"
              >
                <option value="DEVELOPPEUR">👨‍💻 DEVELOPPEUR</option>
                <option value="ANALYSTE">📊 ANALYSTE</option>
                <option value="MANAGER">👔 MANAGER</option>
                <option value="ADMIN">⚙️ ADMIN</option>
              </select>
            </div>
          </div>
          
          <div class="flex space-x-3 mt-6">
            <button 
              @click="isModalOpen = false" 
              class="flex-1 px-4 py-3 bg-slate-700 hover:bg-slate-600 text-white font-medium rounded-lg transition-all"
            >
              Annuler
            </button>
            <button 
              @click="savePosteChange" 
              class="flex-1 px-4 py-3 bg-gradient-to-r from-purple-600 to-pink-600 hover:from-purple-500 hover:to-pink-500 text-white font-semibold rounded-lg transition-all shadow-lg"
            >
              Sauvegarder
            </button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useAuth } from '~/composables/useAuth';

const { user } = useAuth();

const stats = ref({
  totalUsers: 0,
  pendingRequests: 0,
  activeProcesses: 0
});

const users = ref([]);
const loadingUsers = ref(false);
const isModalOpen = ref(false);
const userToEdit = ref(null);
const newPoste = ref('');

const fetchStats = async () => {
  stats.value = {
    totalUsers: 5,
    pendingRequests: 2,
    activeProcesses: 1
  };
};

const fetchUsers = async () => {
  loadingUsers.value = true;
  try {
    const response = await fetch(`http://localhost:8080/api/admin/users`);
    users.value = await response.json();
  } catch (e) {
    console.error("Erreur:", e);
  } finally {
    loadingUsers.value = false;
  }
};

const deleteUser = async (id) => {
  if (!confirm(`Confirmer la suppression de l'utilisateur #${id} ?`)) return;
  try {
    const response = await fetch(`http://localhost:8080/api/admin/users/${id}`, { method: 'DELETE' });
    if (!response.ok) throw new Error("Échec");
    alert(`✅ Utilisateur #${id} supprimé`);
    await fetchUsers();
  } catch (e) {
    alert("❌ Erreur: " + e.message);
  }
};

const savePosteChange = async () => {
  if (!userToEdit.value || !newPoste.value) return;

  try {
    const response = await fetch(`http://localhost:8080/api/admin/users/${userToEdit.value.id}/poste`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ poste: newPoste.value }),
    });

    if (!response.ok) throw new Error("Échec");

    alert(`✅ Poste de ${userToEdit.value.personne.prenom} mis à jour`);
    isModalOpen.value = false;
    await fetchUsers();
  } catch (e) {
    alert("❌ Erreur: " + e.message);
  }
};

const openEditModal = (user) => {
  userToEdit.value = user;
  newPoste.value = user.personne.poste;
  isModalOpen.value = true;
};

const getPosteClass = (poste) => {
  const classes = {
    'MANAGER': 'bg-purple-500 bg-opacity-20 text-purple-300 border border-purple-500',
    'DEVELOPPEUR': 'bg-indigo-500 bg-opacity-20 text-indigo-300 border border-indigo-500',
    'ANALYSTE': 'bg-cyan-500 bg-opacity-20 text-cyan-300 border border-cyan-500',
    'ADMIN': 'bg-pink-500 bg-opacity-20 text-pink-300 border border-pink-500'
  };
  return classes[poste] || 'bg-gray-500 bg-opacity-20 text-gray-300';
};

onMounted(() => {
  if (user.value) {
    fetchStats();
    fetchUsers();
  }
});
</script>