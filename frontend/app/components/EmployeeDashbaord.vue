<template>
  <div>
    <h3>Nouvelle Demande de Congés</h3>
    <form @submit.prevent="createDraft" class="form-draft">
      <input type="number" v-model.number="newRequestDays" placeholder="Nombre de jours" min="1" required>
      <input type="number" v-model.number="managerId" placeholder="ID du Manager (ex: 2)" required>
      <button type="submit" :disabled="loadingDraft">Créer Brouillon</button>
    </form>
    <p v-if="draftMessage" :style="{ color: draftSuccess ? 'green' : 'red' }">{{ draftMessage }}</p>

    <h3>Liste de Mes Demandes</h3>
    <button @click="fetchRequests" :disabled="loadingList">Actualiser</button>

    <table v-if="requests.length">
      <thead>
        <tr>
          <th>ID</th>
          <th>Jours</th>
          <th>Manager</th>
          <th>Statut</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="req in requests" :key="req.id">
          <td>{{ req.id }}</td>
          <td>{{ req.numberOfDays }}</td>
          <td>{{ req.manager.prenom }} {{ req.manager.nom }}</td>
          <td :class="getStatusClass(req.statut)">{{ req.statut }}</td>
          <td>
            <div v-if="req.statut === 'BROUILLON'">
              <button @click="editDraft(req.id, req.numberOfDays + 1)">Modifier (+1 j)</button>
              <button @click="deleteDraft(req.id)">Supprimer</button>
              <button @click="sendRequest(req.id)" class="send-btn">Envoyer</button>
            </div>
            <div v-else>
              En cours...
            </div>
          </td>
        </tr>
      </tbody>
    </table>
    <p v-else-if="!loadingList">Aucune demande trouvée.</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useAuth } from '~/composables/useAuth';

const { user } = useAuth();
const requests = ref([]);
const loadingList = ref(false);
const loadingDraft = ref(false);
const draftMessage = ref('');
const draftSuccess = ref(false);

const newRequestDays = ref(1);
const managerId = ref(2); // ID de Personne du Manager de test (à adapter)

// --- API Calls ---

const fetchRequests = async () => {
  loadingList.value = true;
  try {
    // Utilisez l'ID de la Personne pour le filtre côté backend
    const response = await fetch(`http://localhost:8080/api/my-requests?userId=${user.value.id}`); 
    requests.value = await response.json();
  } catch (e) {
    console.error("Erreur de chargement des demandes:", e);
  } finally {
    loadingList.value = false;
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
    
    if (!response.ok) throw new Error("Erreur de création de brouillon.");
    
    draftMessage.value = "Brouillon créé avec succès !";
    draftSuccess.value = true;
    newRequestDays.value = 1; // Reset
    await fetchRequests(); // Recharger la liste
  } catch (e) {
    draftMessage.value = e.message || "Échec de la création du brouillon.";
    draftSuccess.value = false;
  } finally {
    loadingDraft.value = false;
  }
};

const sendRequest = async (id) => {
  if (!confirm(`Confirmer l'envoi de la demande ${id} au manager ?`)) return;
  try {
    const response = await fetch(`http://localhost:8080/api/requests/${id}/send`, { method: 'POST' });
    if (!response.ok) throw new Error("Échec de l'envoi.");
    alert(`Demande ${id} envoyée ! Le processus Camunda est démarré.`);
    await fetchRequests();
  } catch (e) {
    alert("Erreur: " + e.message);
  }
};

const deleteDraft = async (id) => {
  if (!confirm(`Confirmer la suppression du brouillon ${id} ?`)) return;
  try {
    const response = await fetch(`http://localhost:8080/api/requests/${id}`, { method: 'DELETE' });
    if (!response.ok) throw new Error("Échec de la suppression.");
    alert(`Brouillon ${id} supprimé.`);
    await fetchRequests();
  } catch (e) {
    alert("Erreur: " + e.message);
  }
};

const editDraft = async (id, newDays) => {
    // Ceci est un exemple simple de modification
    try {
        const response = await fetch(`http://localhost:8080/api/requests/${id}?days=${newDays}`, { method: 'PUT' });
        if (!response.ok) throw new Error("Échec de la modification.");
        alert(`Brouillon ${id} mis à jour à ${newDays} jours.`);
        await fetchRequests();
    } catch (e) {
        alert("Erreur: " + e.message);
    }
};


// --- Style et Initialisation ---

const getStatusClass = (statut) => {
  switch(statut) {
    case 'BROUILLON': return 'status-draft';
    case 'EN_ATTENTE': return 'status-pending';
    case 'APPROUVEE': return 'status-approved';
    case 'REJETEE': return 'status-rejected';
    default: return '';
  }
};

onMounted(() => {
  if (user.value) {
    fetchRequests();
  }
});
</script>

<style scoped>
table { width: 100%; border-collapse: collapse; margin-top: 20px; }
th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
.status-draft { background-color: #ffeaa7; }
.status-pending { background-color: #a29bfe; color: white; }
.status-approved { background-color: #00b894; color: white; }
.status-rejected { background-color: #d63031; color: white; }
.send-btn { background-color: #007bff; color: white; }
.form-draft input { margin-right: 10px; padding: 5px; }
.form-draft button { padding: 5px 10px; }
</style>