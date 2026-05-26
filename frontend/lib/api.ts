// Configuración de la API de Spring Boot
const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api'

export interface Atraccion {
  id: string
  nombre: string
  tipo: string
  capacidadMaxima: number
  tiempoEsperaMinutos: number
  estado: 'ACTIVA' | 'CERRADA' | 'EN_MANTENIMIENTO'
  personasEnFila: number
  contadorVisitantes?: number
  motivoCierre?: string
  zonaId?: string
  filaPausada?: boolean
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
  ocupacion?: number
  cupos?: number
}

export interface Estadisticas {
  totalAtracciones: number
  atraccionesActivas: number
  totalPersonasEnFila: number
  tiempoPromedioEspera: number
  capacidadParque?: number
  ocupacionParque?: number
  ticketsVendidos?: number
  ingresosTickets?: number
}

export interface ZonaAforo {
  id: string
  nombre: string
  capacidad: number
  ocupacion: number
  cupos: number
}

export interface AforoParque {
  capacidadParque: number
  ocupacionParque: number
  cuposParque: number
  ticketsVendidos: number
  ingresosTickets: number
  zonas: ZonaAforo[]
}

export interface CompraTicketPayload {
  nombre: string
  documento: string
  edad: number
  estatura: number
  saldo: number
  tipoTicket: 'GENERAL' | 'FAMILIAR' | 'FAST_PASS'
  zonaIngresoId: string
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

export interface ResultadoRuta {
  origen: string
  destino: string
  ruta?: string[]
  cantidadParadas?: number
  distanciaTotal?: number
  algoritmo?: string
  mensaje?: string
}

// Funciones para el API de Atracciones
export async function getAtracciones(): Promise<Atraccion[]> {
  const res = await fetch(`${API_BASE_URL}/parque/atracciones`)
  if (!res.ok) throw new Error('Error al obtener atracciones')
  return res.json()
}

export async function getAtraccion(id: string | number): Promise<Atraccion> {
  const res = await fetch(`${API_BASE_URL}/parque/atracciones/${id}`)
  if (!res.ok) throw new Error('Error al obtener atracción')
  return res.json()
}

export async function actualizarEstadoAtraccion(id: string | number, estado: string): Promise<Atraccion> {
  const res = await fetch(`${API_BASE_URL}/parque/atracciones/${id}/estado`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ estado })
  })
  if (!res.ok) throw new Error('Error al actualizar estado')
  return res.json()
}

export async function registrarRevisionAtraccion(id: string | number): Promise<Atraccion> {
  const res = await fetch(`${API_BASE_URL}/parque/atracciones/${id}/revision`, {
    method: 'POST'
  })
  if (!res.ok) throw new Error('Error al registrar revision')
  return res.json()
}

export async function procesarFilaAtraccion(id: string | number) {
  const res = await fetch(`${API_BASE_URL}/parque/atracciones/${id}/procesar-fila`, {
    method: 'POST'
  })
  if (!res.ok) throw new Error('Error al procesar fila')
  return res.json()
}

export async function generarVisitantesDemo(id: string | number, cantidad: number) {
  const res = await fetch(`${API_BASE_URL}/parque/atracciones/${id}/fila/demo`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ cantidad })
  })
  if (!res.ok) throw new Error('Error al generar visitantes demo')
  return res.json()
}

export async function actualizarPausaFila(id: string | number, pausada: boolean) {
  const res = await fetch(`${API_BASE_URL}/parque/atracciones/${id}/fila/pausa`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ pausada })
  })
  if (!res.ok) throw new Error('Error al actualizar pausa de fila')
  return res.json()
}

// Funciones para Fila Virtual
export async function unirseAFila(
  id: string,
  nombre: string,
  tipoTicket: string,
  datosVisitante?: { edad?: number; estatura?: number; saldo?: number }
): Promise<ResultadoFila> {
  const res = await fetch(`${API_BASE_URL}/parque/atracciones/${id}/fila`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ nombre, tipoTicket, ...datosVisitante })
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

export async function eliminarAtraccion(id: string | number): Promise<void> {
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

export async function calcularRuta(origen: string, destino: string): Promise<ResultadoRuta> {
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

export async function cargarEscenarioInicial() {
  const res = await fetch(`${API_BASE_URL}/datos/cargar-escenario`)
  if (!res.ok) throw new Error("Error al cargar escenario inicial")
  return res.json()
}

export async function getAforo(): Promise<AforoParque> {
  const res = await fetch(`${API_BASE_URL}/aforo`)
  if (!res.ok) throw new Error("Error al obtener aforo")
  return res.json()
}

export async function comprarTicket(payload: CompraTicketPayload) {
  const res = await fetch(`${API_BASE_URL}/tickets/comprar`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  })
  if (!res.ok) throw new Error("Error al comprar ticket")
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

export async function getReporteIngresosDiarios() {
  const res = await fetch(`${API_BASE_URL}/reportes/ingresos-diarios`)
  if (!res.ok) throw new Error("Error al obtener ingresos diarios")
  return res.json()
}

export async function getReporteAlertasMantenimiento() {
  const res = await fetch(`${API_BASE_URL}/reportes/alertas-mantenimiento`)
  if (!res.ok) throw new Error("Error al obtener alertas de mantenimiento")
  return res.json()
}

export async function getReporteIncidentesOperativos() {
  const res = await fetch(`${API_BASE_URL}/reportes/incidentes-operativos`)
  if (!res.ok) throw new Error("Error al obtener incidentes operativos")
  return res.json()
}

export async function getNotificaciones() {
  const res = await fetch(`${API_BASE_URL}/notificaciones`)
  if (!res.ok) throw new Error("Error al obtener notificaciones")
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

