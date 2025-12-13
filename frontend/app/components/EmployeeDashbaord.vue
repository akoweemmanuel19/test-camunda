<template>
  <div class="space-y-6">
    
    <!-- Create Request Card -->
    <div class="relative overflow-hidden bg-gradient-to-br from-purple-900 to-indigo-900 rounded-2xl shadow-2xl p-6 border border-purple-700">
      <div class="absolute top-0 right-0 w-64 h-64 bg-purple-500 rounded-full filter blur-3xl opacity-20 -mr-32 -mt-32"></div>
      
      <div class="relative">
        <div class="flex items-center space-x-3 mb-4">
          <div class="w-10 h-10 bg-purple-500 bg-opacity-30 rounded-lg flex items-center justify-center">
            <svg class="w-6 h-6 text-purple-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
          </div>
          <h3 class="text-xl font-bold text-white">Nouvelle Demande de Congés</h3>
        </div>
        
        <form @submit.prevent="createDraft" class="flex flex-col sm:flex-row gap-3">
          <input 
            type="number" 
            v-model.number="newRequestDays" 
            placeholder="Nombre de jours" 
            min="1" 
            required
            class="flex-1 px-4 py-3 bg-slate-800 bg-opacity-50 border border-slate-600 rounded-lg text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-purple-500 transition-all"
          >
          
          <select 
            v-model.number="managerId" 
            required
            class="flex-1 px-4 py-3 bg-slate-800 bg-opacity-50 border border-slate-600 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-purple-500 transition-all"
          >
            <option value="" disabled>Choisir un manager</option>
            <option v-for="manager in managers" :key="manager.id" :value="manager.id">
              {{ manager.prenom }} {{ manager.nom }}
            </option>
          </select>
          
          <button 
            type="submit" 
            :disabled="loadingDraft"
            class="px-6 py-3 bg-gradient-to-r from-purple-600 to-pink-600 hover:from-purple-500 hover:to-pink-500 text-white font-semibold rounded-lg transition-all duration-300 transform hover:scale-105 disabled:opacity-50 shadow-lg flex items-center space-x-2"
          >
            <svg v-if="loadingDraft" class="animate-spin h-5 w-5" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            <span>{{ loadingDraft ? 'Création...' : 'Créer' }}</span>
          </button>
        </form>
        
        <div v-if="draftMessage" :class="['mt-4 p-4 rounded-lg flex items-center space-x-3', draftSuccess ? 'bg-green-500 bg-opacity-20 border border-green-500' : 'bg-red-500 bg-opacity-20 border border-red-500']">
          <svg class="w-5 h-5 flex-shrink-0" :class="draftSuccess ? 'text-green-400' : 'text-red-400'" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path v-if="draftSuccess" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            <path v-else stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          <span :class="draftSuccess ? 'text-green-300' : 'text-red-300'" class="text-sm font-medium">{{ draftMessage }}</span>
        </div>
      </div>
    </div>

    <!-- Requests Table -->
    <div class="bg-slate-800 bg-opacity-50 backdrop-blur-xl rounded-2xl shadow-2xl border border-slate-700 overflow-hidden">
      
      <div class="flex justify-between items-center p-6 border-b border-slate-700">
        <div class="flex items-center space-x-3">
          <div class="w-10 h-10 bg-indigo-500 bg-opacity-30 rounded-lg flex items-center justify-center">
            <svg class="w-6 h-6 text-indigo-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
          </div>
          <h3 class="text-xl font-bold text-white">Mes Demandes</h3>
        </div>
        <button 
          @click="fetchRequests" 
          :disabled="loadingList"
          class="flex items-center space-x-2 px-4 py-2 bg-slate-700 hover:bg-slate-600 text-white rounded-lg transition-all duration-300 transform hover:scale-105 disabled:opacity-50"
        >
          <svg class="w-4 h-4" :class="{ 'animate-spin': loadingList }" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
          </svg>
          <span class="text-sm font-medium">{{ loadingList ? 'Chargement...' : 'Actualiser' }}</span>
        </button>
      </div>

      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-slate-700">
          <thead class="bg-slate-900 bg-opacity-50">
            <tr>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-300 uppercase tracking-wider">ID</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-300 uppercase tracking-wider">Jours</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-300 uppercase tracking-wider">Manager</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-300 uppercase tracking-wider">Statut</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-300 uppercase tracking-wider">Actions</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-700">
            <tr v-for="req in requests" :key="req.id" class="hover:bg-slate-700 hover:bg-opacity-30 transition-colors">
              <td class="px-6 py-4 whitespace-nowrap text-sm font-mono text-purple-400">#{{ req.id }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-300 font-medium">{{ req.numberOfDays }} jour(s)</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-400">{{ req.manager.prenom }} {{ req.manager.nom }}</td>
              
              <td class="px-6 py-4 whitespace-nowrap">
                <span :class="getStatusClass(req.statut)" class="px-3 py-1 inline-flex text-xs leading-5 font-bold rounded-full">
                  {{ getStatusLabel(req.statut) }}
                </span>
              </td>

              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                <div v-if="req.statut === 'BROUILLON'" class="flex space-x-2">
                  <button @click="editDraft(req.id, req.numberOfDays + 1)" class="p-2 text-indigo-400 hover:text-indigo-300 hover:bg-indigo-500 hover:bg-opacity-20 rounded-lg transition-all" title="Modifier">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                    </svg>
                  </button>
                  <button @click="deleteDraft(req.id)" class="p-2 text-red-400 hover:text-red-300 hover:bg-red-500 hover:bg-opacity-20 rounded-lg transition-all" title="Supprimer">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                    </svg>
                  </button>
                  <button @click="sendRequest(req.id)" class="px-3 py-2 text-xs font-semibold text-white bg-gradient-to-r from-green-600 to-emerald-600 hover:from-green-500 hover:to-emerald-500 rounded-lg transition-all flex items-center space-x-1">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8" />
                    </svg>
                    <span>Envoyer</span>
                  </button>
                </div>
                <div v-else class="text-xs text-slate-500 italic">Traité</div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <p v-if="!requests.length && !loadingList" class="p-8 text-center text-slate-400">
        <svg class="w-12 h-12 mx-auto mb-3 text-slate-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
        </svg>
        Aucune demande pour le moment. Créez-en une nouvelle !
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useAuth } from '~/composables/useAuth';

const { user } = useAuth();
const requests = ref([]);
const managers = ref([]);
const loadingList = ref(false);
const loadingDraft = ref(false);
const draftMessage = ref('');
const draftSuccess = ref(false);
const newRequestDays = ref(1);
const managerId = ref('');

const fetchRequests = async () => {
  loadingList.value = true;
  try {
    const response = await fetch(`http://localhost:8080/api/my-requests?userId=${user.value.id}`);
    requests.value = await response.json();
  } catch (e) {
    console.error("Erreur de chargement:", e);
  } finally {
    loadingList.value = false;
  }
};

const fetchManagers = async () => {
  try {
    const response = await fetch(`http://localhost:8080/api/manager`);
    if (response.ok) {
      managers.value = await response.json();
    }
  } catch (e) {
    console.error("Erreur de chargement des managers:", e);
  }
};

const createDraft = async () => {
  loadingDraft.value = true;
  draftMessage.value = '';
  try {
    const response = await fetch(`http://localhost:8080/api/requests/draft?userId=${user.value.id}&days=${newRequestDays.value}&managerId=${managerId.value}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
    });
    
    if (!response.ok) throw new Error("Erreur de création");
    
    draftMessage.value = "✅ Brouillon créé avec succès !";
    draftSuccess.value = true;
    newRequestDays.value = 1;
    managerId.value = '';
    await fetchRequests();
  } catch (e) {
    draftMessage.value = "❌ " + e.message;
    draftSuccess.value = false;
  } finally {
    loadingDraft.value = false;
  }
};

const sendRequest = async (id) => {
  if (!confirm(`Envoyer la demande #${id} au manager ?`)) return;
  try {
    const response = await fetch(`http://localhost:8080/api/requests/${id}/send`, { method: 'POST' });
    if (!response.ok) throw new Error("Échec de l'envoi");
    alert(`✅ Demande #${id} envoyée avec succès !`);
    await fetchRequests();
  } catch (e) {
    alert("❌ Erreur: " + e.message);
  }
};

const deleteDraft = async (id) => {
  if (!confirm(`Supprimer le brouillon #${id} ?`)) return;
  try {
    const response = await fetch(`http://localhost:8080/api/requests/${id}`, { method: 'DELETE' });
    if (!response.ok) throw new Error("Échec");
    alert(`✅ Brouillon #${id} supprimé`);
    await fetchRequests();
  } catch (e) {
    alert("❌ Erreur: " + e.message);
  }
};

const editDraft = async (id, newDays) => {
  try {
    const response = await fetch(`http://localhost:8080/api/requests/${id}?days=${newDays}`, { method: 'PUT' });
    if (!response.ok) throw new Error("Échec");
    alert(`✅ Brouillon #${id} mis à jour`);
    await fetchRequests();
  } catch (e) {
    alert("❌ Erreur: " + e.message);
  }
};

const getStatusClass = (statut) => {
  const classes = {
    'BROUILLON': 'bg-yellow-500 bg-opacity-20 text-yellow-300 border border-yellow-500',
    'EN_ATTENTE': 'bg-blue-500 bg-opacity-20 text-blue-300 border border-blue-500',
    'APPROUVEE': 'bg-green-500 bg-opacity-20 text-green-300 border border-green-500',
    'REJETEE': 'bg-red-500 bg-opacity-20 text-red-300 border border-red-500'
  };
  return classes[statut] || 'bg-gray-500 bg-opacity-20 text-gray-300';
};

const getStatusLabel = (statut) => {
  const labels = {
    'BROUILLON': '📝 Brouillon',
    'EN_ATTENTE': '⏳ En attente',
    'APPROUVEE': '✅ Approuvée',
    'REJETEE': '❌ Rejetée'
  };
  return labels[statut] || statut;
};

onMounted(() => {
  if (user.value) {
    fetchRequests();
    fetchManagers();
  }
});
</script>