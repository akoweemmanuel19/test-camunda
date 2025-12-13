<template>
  <div>
    <button @click="fetchManagerRequests" :disabled="loading">Actualiser les demandes</button>

    <table v-if="requests.length">
      <thead>
        <tr>
          <th>ID</th>
          <th>Employé</th>
          <th>Jours</th>
          <th>Statut</th>
          <th>Commentaires Manager</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="req in requests" :key="req.id">
          <td>{{ req.id }}</td>
          <td>{{ req.employee.prenom }} {{ req.employee.nom }}</td>
          <td>{{ req.numberOfDays }}</td>
          <td :class="getStatusClass(req.statut)">{{ req.statut }}</td>
          <td>
            <textarea v-model="decisions[req.id].comment" placeholder="Commentaire..."></textarea>
          </td>
          <td>
            <div v-if="req.statut === 'EN_ATTENTE'">
              <button @click="makeDecision(req.id, true)" class="approve-btn">Approuver</button>
              <button @click="makeDecision(req.id, false)" class="reject-btn">Rejeter</button>
            </div>
            <div v-else>
                Traité: {{ req.statut }}
            </div>
          </td>
        </tr>
      </tbody>
    </table>
    <p v-else-if="!loading">Aucune demande à traiter.</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useAuth } from '~/composables/useAuth';

const { user } = useAuth();
const requests = ref([]);
const loading = ref(false);
// État pour stocker les commentaires avant la décision
const decisions = ref({});

const fetchManagerRequests = async () => {
  loading.value = true;
  try {
    // L'ID de la Personne du Manager est utilisé pour filtrer
    const response = await fetch(`http://localhost:8080/api/manager/requests?userId=${user.value.id}`); 
    requests.value = await response.json();
    
    // Initialiser l'objet de décisions
    requests.value.forEach(req => {
      if (!decisions.value[req.id]) {
        decisions.value[req.id] = { comment: '' };
      }
    });

  } catch (e) {
    console.error("Erreur de chargement des demandes manager:", e);
  } finally {
    loading.value = false;
  }
};

const makeDecision = async (requestId, approved) => {
  const comment = decisions.value[requestId].comment;
  const action = approved ? 'Approbation' : 'Rejet';
  
  if (!confirm(`Confirmer ${action} pour la demande ${requestId} ?`)) return;

  try {
    const response = await fetch(`http://localhost:8080/api/manager/requests/${requestId}/decide`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ approved: approved, comment: comment }),
    });

    if (!response.ok) throw new Error("Échec de la décision manager.");
    
    alert(`${action} enregistrée. Le workflow Camunda est passé à l'étape suivante.`);
    await fetchManagerRequests(); // Recharger la liste

  } catch (e) {
    alert("Erreur: " + e.message);
  }
};

const getStatusClass = (statut) => {
  switch(statut) {
    case 'EN_ATTENTE': return 'status-pending';
    case 'APPROUVEE': return 'status-approved';
    case 'REJETEE': return 'status-rejected';
    default: return '';
  }
};

onMounted(() => {
  if (user.value) {
    fetchManagerRequests();
  }
});
</script>

<style scoped>
.approve-btn { background-color: #00b894; color: white; }
.reject-btn { background-color: #d63031; color: white; margin-left: 5px; }
/* Réutiliser les styles de statut de l'EmployeeDashboard */
</style>