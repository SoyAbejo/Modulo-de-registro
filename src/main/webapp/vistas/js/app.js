/* ═══════════════════════════════════════════════════════════════════════════
   app.js — JavaScript compartido de PetServices
   ─────────────────────────────────────────────────────────────────────────
   Funcionalidades:
   1. Menú lateral móvil (abrir/cerrar sidebar)
   2. Filtro en tiempo real para las tablas (busqueda por texto)
   3. Confirmación genérica de eliminación de registros

   Proyecto: PetServices - SENA GA7-220501096
   ═══════════════════════════════════════════════════════════════════════════ */

/* ── 1. Menú lateral (móvil) ─────────────────────────────────────────────── */
function alternarMenu() {
    var sidebar = document.getElementById("sidebar");
    if (sidebar) {
        sidebar.classList.toggle("abierta");
    }
}

/* Cerrar el menú al hacer clic fuera (solo en móvil) */
document.addEventListener("click", function (e) {
    var sidebar = document.getElementById("sidebar");
    if (!sidebar) return;
    var dentro = sidebar.contains(e.target);
    var toggle = e.target.closest(".menu-toggle");
    if (!dentro && !toggle && sidebar.classList.contains("abierta")) {
        sidebar.classList.remove("abierta");
    }
});

/* ── 2. Filtro en tiempo real de tablas ──────────────────────────────────── */
/*
 * Cada input con atributo data-filtro="id-de-la-tabla" filtra las filas
 * de la tabla con ese id. Ej:
 *   <input class="busqueda-input" data-filtro="tabla-clientes" placeholder="Buscar...">
 *   <table id="tabla-clientes">...
 */
function iniciarFiltros() {
    var inputs = document.querySelectorAll("input[data-filtro]");
    inputs.forEach(function (input) {
        input.addEventListener("input", function () {
            var termino = input.value.toLowerCase().trim();
            var tabla   = document.getElementById(input.getAttribute("data-filtro"));
            if (!tabla) return;

            var filas = tabla.querySelectorAll("tbody tr");
            var visibles = 0;
            filas.forEach(function (fila) {
                // Las filas de "estado vacío" (colspan) siempre se ocultan al filtrar
                if (fila.classList.contains("fila-vacia")) {
                    fila.style.display = (termino === "") ? "" : "none";
                    return;
                }
                var texto = fila.textContent.toLowerCase();
                var visible = (termino === "") || texto.indexOf(termino) !== -1;
                fila.style.display = visible ? "" : "none";
                if (visible) visibles++;
            });
        });
    });
}

/* ── 3. Confirmación genérica de eliminación ─────────────────────────────── */
/*
 * Uso: onclick="eliminarRegistro('url?accion=eliminar&id=5', 'Nombre del registro')"
 */
function eliminarRegistro(url, nombre) {
    var mensaje = (nombre && nombre !== "")
        ? '⚠️ ¿Seguro que deseas eliminar "' + nombre + '"?\nEsta acción no se puede deshacer.'
        : "⚠️ ¿Seguro que deseas eliminar este registro?\nEsta acción no se puede deshacer.";
    if (confirm(mensaje)) {
        window.location.href = url;
    }
}

/* ── Inicialización ──────────────────────────────────────────────────────── */
document.addEventListener("DOMContentLoaded", iniciarFiltros);
