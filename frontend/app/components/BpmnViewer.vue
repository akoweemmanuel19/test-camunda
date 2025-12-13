<template>
    <div class="bg-slate-900 border border-slate-700 rounded-lg p-4">
        <div class="flex items-center justify-between mb-4">
            <h4 class="text-lg font-semibold text-white">
                Processus: <span class="text-cyan-400">{{ processKey }}</span>
            </h4>
            <div class="px-3 py-1 bg-yellow-600 rounded-full text-sm font-bold text-white shadow-md">
                Activité: {{ currentActivityId || 'Démarrée' }}
            </div>
        </div>

        <div ref="canvas" class="bpmn-canvas" style="height: 500px; width: 100%;"></div>
    </div>
</template>

<script setup>
import BpmnViewer from 'bpmn-js/lib/Viewer';
import camundaModdleDescriptor from 'camunda-bpmn-moddle/resources/camunda';
import { ref, onMounted, watch } from 'vue';

// Définition des props attendues
const props = defineProps({
    bpmnXml: String,
    currentActivityId: String,
    processKey: String
});

const canvas = ref(null);

let viewer = null;

const renderDiagram = async (xml) => {
    if (!viewer) {
        viewer = new BpmnViewer({
            container: canvas.value,
            moddleExtensions: {
                camunda: camundaModdleDescriptor
            }
        });
    }

    try {
        // AJOUTEZ UN PETIT DELAI POUR ASSURER LE RENDU DU DOM
        await new Promise(resolve => setTimeout(resolve, 50));

        await viewer.importXML(xml);

        // Assurer que le viewer redimensionne correctement
        const canvasElement = viewer.get('canvas');
        canvasElement.zoom('fit-viewport');

        highlightActivity(props.currentActivityId);
    } catch (err) {
        console.error('Erreur lors du rendu du diagramme BPMN', err);
    }
};

const highlightActivity = (activityId) => {
    const canvasElement = viewer.get('canvas');
    const overlays = viewer.get('overlays');

    // 1. Nettoyer les surbrillances et overlays précédents
    canvasElement.removeMarkers();
    overlays.clear();

    if (activityId) {
        // 2. Mettre en surbrillance l'activité en cours (Style du Cockpit Camunda)
        canvasElement.addMarker(activityId, 'highlight-current');
        canvasElement.zoom('fit-viewport');

        // 3. Ajouter un badge ou un icône (optionnel)
        overlays.add(activityId, {
            position: { bottom: 0, right: 0 },
            html: '<div class="bpmn-badge">⚡</div>'
        });
    }
};


onMounted(() => {
    // Initialisation du viewer ici, après que le DOM soit prêt
    viewer = new BpmnViewer({
        container: canvas.value,
        moddleExtensions: {
            camunda: camundaModdleDescriptor
        }
    });

    if (props.bpmnXml) {
        renderDiagram(props.bpmnXml);
    }
});

// Surveiller les changements des props pour mettre à jour le diagramme
watch(() => props.bpmnXml, (newXml) => {
    if (newXml) {
        renderDiagram(newXml);
    }
});

watch(() => props.currentActivityId, (newActivityId) => {
    if (viewer && props.bpmnXml) {
        highlightActivity(newActivityId);
    }
});
</script>

<style>
/* Styles pour la surbrillance (Doit être dans votre CSS global ou dans <style> sans scoped) */
.bpmn-canvas {
    border: 1px solid #334155;
    /* slate-700 */
    border-radius: 8px;
}

.highlight-current:not(.djs-connection) .djs-visual> :nth-child(1) {
    fill: #f59e0b !important;
    /* yellow-500 */
    stroke: #d97706 !important;
    /* yellow-700 */
    stroke-width: 3px !important;
    opacity: 0.8;
}

.bpmn-badge {
    background-color: #ef4444;
    /* red-500 */
    color: white;
    padding: 3px 6px;
    border-radius: 50%;
    font-size: 10px;
    font-weight: bold;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.5);
}
</style>