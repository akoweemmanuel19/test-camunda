<template>
  <div class="space-y-6">
    
    <!-- Header Actions -->
    <div class="flex justify-between items-center">
      <button 
        @click="fetchManagerRequests" 
        :disabled="loading"
        class="flex items-center space-x-2 px-5 py-3 bg-gradient-to-r from-teal-600 to-cyan-600 hover:from-teal-500 hover:to-cyan-500 text-white font-semibold rounded-xl transition-all duration-300 transform hover:scale-105 disabled:opacity-50 shadow-lg"
      >
        <svg class="w-5 h-5" :class="{ 'animate-spin': loading }" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
        </svg>
        <span>{{ loading ? 'Chargement...' : 'Actualiser' }}</span>
      </button>
    </div>

    <!-- Requests Table -->
    <div class="bg-slate-800 bg-opacity-50 backdrop-blur-xl rounded-2xl shadow-2xl border border-slate-700 overflow-hidden">
      
      <div class="p-6 border-b border-slate-700 bg-gradient-to-r from-teal-900 to-cyan-900 bg-opacity-30">
        <div class="flex items-center space-x-3">
          <div class="w-10 h-10 bg-teal-500 bg-opacity-30 rounded-lg flex items-center justify-center">
            <svg class="w-6 h-6 text-teal-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4" />
            </svg>
          </div>
          <div>
            <h3 class="text-xl font-bold text-white">Demandes en Attente</h3>
            <p class="text-sm text-slate-400">Gérez les demandes de votre équipe</p>
          </div>
        </div>
      </div>

      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-slate-700">
          <thead class="bg-slate-900 bg-opacity-50">
            <tr>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-300 uppercase tracking-wider">ID</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-300 uppercase tracking-wider">Employé</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-300 uppercase tracking-wider">Jours</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-300 uppercase tracking-wider">Statut</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-300 uppercase tracking-wider w-1/4">Commentaire</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-300 uppercase tracking-wider">Actions</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-700">
            <tr v-for="req in requests" :key="req.id" class="hover:bg-slate-700 hover:bg-opacity-30 transition-colors">
              <td class="px-6 py-4 whitespace-nowrap text-sm font-mono text-purple-400">#{{ req.id }}</td>
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="flex items-center space-x-3">
                  <div class="w-8 h-8 bg-gradient-to-br from-teal-500 to-cyan-500 rounded-full flex items-center justify-center text-xs font-bold text-white">
                    {{ req.employee.prenom.charAt(0) }}
                  </div>
                  <span class="text-sm text-slate-300 font-medium">{{ req.employee.prenom }} {{ req.employee.nom }}</span>
                </div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-300 font-medium">{{ req.numberOfDays }} jour(s)</td>
              
              <td class="px-6 py-4 whitespace-nowrap">
                <span :class="getStatusClass(req.statut)" class="px-3 py-1 inline-flex text-xs leading-5 font-bold rounded-full">
                  {{ getStatusLabel(req.statut) }}
                </span>
              </td>
              
              <td class="px-6 py-4">
                <textarea 
                  v-model="decisions[req.id].comment" 
                  placeholder="Votre commentaire (optionnel)..."
                  rows="2"
                  class="w-full px-3 py-2 bg-slate-700 bg-opacity-50 border border-slate-600 rounded-lg text-white text-sm placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-teal-500 transition-all resize-none"
                ></textarea>
              </td>
              
              <td class="px-6 py-4 whitespace-nowrap">
                <div v-if="req.statut === 'EN_ATTENTE'" class="flex space-x-2">
                  <button 
                    @click="makeDecision(req.id, true)" 
                    class="group flex items-center space-x-2 px-3 py-2 text-xs font-semibold text-white bg-gradient-to-r from-green-600 to-emerald-600 hover:from-green-500 hover:to-emerald-500 rounded-lg transition-all transform hover:scale-105 shadow-lg"
                    title="Approuver"
                  >
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                    </svg>
                    <span>Approuver</span>
                  </button>
                  <button 
                    @click="makeDecision(req.id, false)" 
                    class="group flex items-center space-x-2 px-3 py-2 text-xs font-semibold text-white bg-gradient-to-r from-red-600 to-rose-600 hover:from-red-500 hover:to-rose-500 rounded-lg transition-all transform hover:scale-105 shadow-lg"
                    title="Rejeter"
                  >
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                    </svg>
                    <span>Rejeter</span>
                  </button>
                </div>
                <div v-else class="flex items-center space-x-2 text-xs text-slate-500 italic">
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                  <span>Traité</span>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <p v-if="!requests.length && !loading" class="p-8 text-center text-slate-400">
        <svg class="w-12 h-12 mx-auto mb-3 text-slate-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        Aucune demande à traiter pour le moment.
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useAuth } from '~/composables/useAuth';

const { user } = useAuth();
const requests = ref([]);
const loading = ref(false);
const decisions = ref({});

const fetchManagerRequests = async () => {
  loading.value = true;
  try {
    const response = await fetch(`http://localhost:8080/api/manager/requests?userId=${user.value.id}`);
    requests.value = await response.json();
    
    requests.value.forEach(req => {
      if (!decisions.value[req.id]) {
        decisions.value[req.id] = { comment: '' };
      }
    });
  } catch (e) {
    console.error("Erreur:", e);
  } finally {
    loading.value = false;
  }
};

const makeDecision = async (requestId, approved) => {
  const comment = decisions.value[requestId].comment;
  const action = approved ? 'approbation' : 'rejet';
  
  if (!confirm(`Confirmer ${action} de la demande #${requestId} ?`)) return;

  try {
    const response = await fetch(`http://localhost:8080/api/manager/requests/${requestId}/decide`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ approved, comment }),
    });

    if (!response.ok) throw new Error("Échec");
    
    alert(`✅ ${action.charAt(0).toUpperCase() + action.slice(1)} enregistrée avec succès !`);
    await fetchManagerRequests();
  } catch (e) {
    alert("❌ Erreur: " + e.message);
  }
};

const getStatusClass = (statut) => {
  const classes = {
    'EN_ATTENTE': 'bg-blue-500 bg-opacity-20 text-blue-300 border border-blue-500',
    'APPROUVEE': 'bg-green-500 bg-opacity-20 text-green-300 border border-green-500',
    'REJETEE': 'bg-red-500 bg-opacity-20 text-red-300 border border-red-500'
  };
  return classes[statut] || 'bg-gray-500 bg-opacity-20 text-gray-300';
};

const getStatusLabel = (statut) => {
  const labels = {
    'EN_ATTENTE': '⏳ En attente',
    'APPROUVEE': '✅ Approuvée',
    'REJETEE': '❌ Rejetée'
  };
  return labels[statut] || statut;
};

onMounted(() => {
  if (user.value) fetchManagerRequests();
});
</script>