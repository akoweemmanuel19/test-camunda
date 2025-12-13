<template>
  <div class="login-container">
    <h2>Connexion Camunda Workflow</h2>
    <form @submit.prevent="handleLogin">
      <input v-model="identifiant" type="text" placeholder="Identifiant (ex: alice_d)" required>
      <input v-model="motDePasse" type="password" placeholder="Mot de passe (ex: pass)" required>
      <button type="submit" :disabled="loading">
        {{ loading ? 'Chargement...' : 'Se connecter' }}
      </button>
      <p v-if="error" class="error-message">{{ error }}</p>
    </form>
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
      body: JSON.stringify({ identifiant: identifiant.value, motDePasse: motDePasse.value }),
    });

    if (!response.ok) {
      throw new Error("Échec de la connexion. Vérifiez les identifiants.");
    }

    const userData = await response.json();
    login(userData);
    
    // Redirection après succès
    router.push('/');

  } catch (err) {
    error.value = err.message || 'Erreur inconnue lors de la connexion.';
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-container { max-width: 400px; margin: 50px auto; padding: 20px; border: 1px solid #ccc; border-radius: 8px; }
input { display: block; width: 100%; padding: 10px; margin-bottom: 15px; box-sizing: border-box; }
button { width: 100%; padding: 10px; background-color: #007bff; color: white; border: none; cursor: pointer; }
.error-message { color: red; margin-top: 10px; }
</style>