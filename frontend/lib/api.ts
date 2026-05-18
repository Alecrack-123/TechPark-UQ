// Configuración de la API de Spring Boot
const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api'

export interface Atraccion {
  id: number
  nombre: string
  tipo: string
  capacidadMaxima: number
  tiempoEsperaMinutos: number
  estado: 'ACTIVA' | 'CERRADA' | 'MANTENIMIENTO'
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
export async function unirseAFila(atraccionId: number, usuarioId: number): Promise<void> {
  const res = await fetch(`${API_BASE_URL}/fila/unirse`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ atraccionId, usuarioId })
  })
  if (!res.ok) throw new Error('Error al unirse a la fila')
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
export async function getEstadisticas(): Promise<EstadisticasParque> {
  const res = await fetch(`${API_BASE_URL}/estadisticas`)
  if (!res.ok) throw new Error('Error al obtener estadísticas')
  return res.json()
}

export async function getEstadisticasPorAtraccion(id: number) {
  const res = await fetch(`${API_BASE_URL}/estadisticas/atraccion/${id}`)
  if (!res.ok) throw new Error('Error al obtener estadísticas de atracción')
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
