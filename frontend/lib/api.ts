// Configuración de la API de Spring Boot
const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api'

export interface Atraccion {
  id: number
  nombre: string
  tipo: string
  capacidadMaxima: number
  tiempoEsperaMinutos: number
  estado: 'ACTIVA' | 'CERRADA' | 'EN_MANTENIMIENTO'
  personasEnFila: number
}

export interface Usuario {
  id: number
  nombre: string
  email: string
  rol: 'USUARIO' | 'ADMIN'
}

export interface EstadisticasParque {
  totalAtracciones: number
  atraccionesActivas: number
  totalPersonasEnFila: number
  tiempoPromedioEspera: number
}

export interface Zona {
  id: string
  nombre: string
  capacidad: number
}

export interface Estadisticas {
  totalAtracciones: number
  atraccionesActivas: number
  totalPersonasEnFila: number
  tiempoPromedioEspera: number
}

export interface ResultadoClima {
  clima: string
  mensaje: string
}

export interface ResultadoFila {
  mensaje?: string
  error?: string
  posicion?: number
  tiempoEstimado?: number
  tipoTicket?: string
}

// Funciones para el API de Atracciones
export async function getAtracciones(): Promise<Atraccion[]> {
  const res = await fetch(`${API_BASE_URL}/parque/atracciones`)
  if (!res.ok) throw new Error('Error al obtener atracciones')
  return res.json()
}

export async function getAtraccion(id: number): Promise<Atraccion> {
  const res = await fetch(`${API_BASE_URL}/parque/atracciones/${id}`)
  if (!res.ok) throw new Error('Error al obtener atracción')
  return res.json()
}

export async function actualizarEstadoAtraccion(id: number, estado: string): Promise<Atraccion> {
  const res = await fetch(`${API_BASE_URL}/parque/atracciones/${id}/estado`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ estado })
  })
  if (!res.ok) throw new Error('Error al actualizar estado')
  return res.json()
}

// Funciones para Fila Virtual
export async function unirseAFila(
  id: string,
  nombre: string,
  tipoTicket: string
): Promise<ResultadoFila> {
  const res = await fetch(`${API_BASE_URL}/parque/atracciones/${id}/fila`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ nombre, tipoTicket })
  })

  if (!res.ok) throw new Error('Error al unirse a la fila')

  return res.json()
}

export async function salirDeFila(atraccionId: number, usuarioId: number): Promise<void> {
  const res = await fetch(`${API_BASE_URL}/fila/salir`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ atraccionId, usuarioId })
  })
  if (!res.ok) throw new Error('Error al salir de la fila')
}

export async function getPosicionEnFila(atraccionId: number, usuarioId: number): Promise<number> {
  const res = await fetch(`${API_BASE_URL}/fila/posicion?atraccionId=${atraccionId}&usuarioId=${usuarioId}`)
  if (!res.ok) throw new Error('Error al obtener posición')
  return res.json()
}

// Funciones para Estadísticas
export async function getEstadisticas(): Promise<Estadisticas> {
  const res = await fetch(`${API_BASE_URL}/estadisticas`)
  if (!res.ok) throw new Error('Error al obtener estadísticas')
  return res.json()
}

export async function getEstadisticasPorAtraccion(id: number) {
  const res = await fetch(`${API_BASE_URL}/estadisticas/atraccion/${id}`)
  if (!res.ok) throw new Error('Error al obtener estadísticas de atracción')
  return res.json()
}

// Funciones para Zonas
export async function getZonas(): Promise<Zona[]> {
  const res = await fetch(`${API_BASE_URL}/zonas`)
  if (!res.ok) throw new Error('Error al obtener zonas')
  return res.json()
}

// Funciones para Clima
export async function activarClima(tipo: 'soleado' | 'lluvia' | 'tormenta'): Promise<ResultadoClima> {
  const res = await fetch(`${API_BASE_URL}/clima/${tipo}`)
  if (!res.ok) throw new Error('Error al cambiar clima')
  return res.json()
}

// Funciones para Usuarios (Admin)
export async function getUsuarios(): Promise<Usuario[]> {
  const res = await fetch(`${API_BASE_URL}/usuarios`)
  if (!res.ok) throw new Error('Error al obtener usuarios')
  return res.json()
}

export async function crearAtraccion(atraccion: Omit<Atraccion, 'id'>): Promise<Atraccion> {
  const res = await fetch(`${API_BASE_URL}/parque/atracciones`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(atraccion)
  })
  if (!res.ok) throw new Error('Error al crear atracción')
  return res.json()
}

export async function eliminarAtraccion(id: number): Promise<void> {
  const res = await fetch(`${API_BASE_URL}/parque/atracciones/${id}`, {
    method: 'DELETE'
  })
  if (!res.ok) throw new Error('Error al eliminar atracción')
}

export async function buscarAtraccionPorId(id: string) {
  const res = await fetch(`${API_BASE_URL}/parque/atracciones/buscar/${id}`)
  if (!res.ok) throw new Error('Error al buscar atraccion')
  return res.json()
}

export async function calcularRuta(origen: string, destino: string) {
  const res = await fetch(`${API_BASE_URL}/rutas/${origen}/${destino}`)

  if (!res.ok) {
    throw new Error("Error al calcular ruta")
  }

  return res.json()
}

export async function cargarVisitante() {
  const res = await fetch(`${API_BASE_URL}/visitante/cargar`)
  if (!res.ok) throw new Error("Error al cargar visitante")
  return res.json()
}

export async function getConexionesGrafo() {
  const res = await fetch(`${API_BASE_URL}/rutas/conexiones`)
  if (!res.ok) throw new Error("Error al obtener conexiones del grafo")
  return res.json()
}

export async function getReporteTotalVisitantes() {
  const res = await fetch(`${API_BASE_URL}/reportes/total-visitantes`)
  if (!res.ok) throw new Error("Error al obtener total de visitantes")
  return res.json()
}

export async function getReporteAtraccionMasVisitada() {
  const res = await fetch(`${API_BASE_URL}/reportes/atraccion-mas-visitada`)
  if (!res.ok) throw new Error("Error al obtener atraccion mas visitada")
  return res.json()
}

export async function getReporteFilaMasLarga() {
  const res = await fetch(`${API_BASE_URL}/reportes/fila-mas-larga`)
  if (!res.ok) throw new Error("Error al obtener fila mas larga")
  return res.json()
}

export async function getReporteCierresClima() {
  const res = await fetch(`${API_BASE_URL}/reportes/cierres-clima`)
  if (!res.ok) throw new Error("Error al obtener cierres por clima")
  return res.json()
}

export async function getAnalisisGrafo() {
  const res = await fetch(`${API_BASE_URL}/rutas/analisis`)

  if (!res.ok) {
    throw new Error("Error al obtener analisis del grafo")
  }

  return res.json()
}

export async function getJerarquiaAdmin() {
  const res = await fetch(`${API_BASE_URL}/admin/jerarquia`)

  if (!res.ok) {
    throw new Error("Error al obtener jerarquia")
  }

  return res.json()
}

