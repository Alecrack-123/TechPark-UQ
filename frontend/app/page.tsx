"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import {
  Map, User, Settings, BarChart3, Clock, Users, AlertTriangle,
  CheckCircle2, XCircle, Zap, Timer, TrendingUp, Ticket,
  Navigation, CloudSun, Menu, X, CloudRain, CloudLightning, Sun,
} from "lucide-react"
import { cn } from "@/lib/utils"
import {
  getAtracciones, getZonas, getEstadisticas, activarClima, buscarAtraccionPorId, unirseAFila,
  type Atraccion, type Zona, type Estadisticas
} from "@/lib/api"

const navItems = [
  { id: "inicio", label: "Inicio / Mapa", icon: Map },
  { id: "usuario", label: "Panel Usuario", icon: User },
  { id: "admin", label: "Administración", icon: Settings },
  { id: "stats", label: "Estadísticas", icon: BarChart3 },
]

export default function TechParkDashboard() {
  const [activePanel, setActivePanel] = useState("inicio")
  const [atracciones, setAtracciones] = useState<Atraccion[]>([])
  const [zonas, setZonas] = useState<Zona[]>([])
  const [estadisticas, setEstadisticas] = useState<Estadisticas | null>(null)
  const [loading, setLoading] = useState(true)
  const [sidebarOpen, setSidebarOpen] = useState(false)
  const [busqueda, setBusqueda] = useState("")
  const [resultadoBusqueda, setResultadoBusqueda] = useState<any>(null)
  const [climaActual, setClimaActual] = useState("SOLEADO")
  const [mensajeClima, setMensajeClima] = useState("Todas las atracciones funcionan normalmente")
  const [loadingClima, setLoadingClima] = useState(false)
  const [logueado, setLogueado] = useState(false)
  const [usuarioLogin, setUsuarioLogin] = useState("")
  const [rolLogin, setRolLogin] = useState("visitante")

  useEffect(() => {
    const cargarDatos = async () => {
      setLoading(true)
      try {
        const [dataAtracciones, dataZonas, dataStats] = await Promise.all([
          getAtracciones(),
          getZonas(),
          getEstadisticas(),
        ])
        setAtracciones(dataAtracciones)
        setZonas(dataZonas)
        setEstadisticas(dataStats)
      } catch (error) {
        console.error("Error cargando datos:", error)
        setAtracciones([])
      } finally {
        setLoading(false)
      }
    }
    cargarDatos()
  }, [])

  const buscarAtraccion = async () => {
    if (busqueda.trim() === "") return
    try {
      const data = await buscarAtraccionPorId(busqueda)
      setResultadoBusqueda(data)
    } catch (error) {
      console.error("Error buscando atraccion:", error)
    }
  }

  const iniciarSesion = () => {
    if (usuarioLogin.trim() === "") {
      alert("Ingrese un nombre de usuario")
      return
    }
    setLogueado(true)
    if (rolLogin === "visitante") setActivePanel("usuario")
    else if (rolLogin === "operador") setActivePanel("operador")
    else setActivePanel("admin")
  }

  const manejarFila = async (id: string) => {
    try {
      const data = await unirseAFila(id, "Juan", "FAST_PASS")
      alert(
        data.mensaje +
        "\nPosicion: " + data.posicion +
        "\nTiempo estimado: " + data.tiempoEstimado + " min"
      )
      const nuevasAtracciones = await getAtracciones()
      setAtracciones(nuevasAtracciones)
    } catch (error) {
      console.error(error)
      alert("Error al unirse a la fila")
    }
  }

  const cambiarClima = async (tipo: 'soleado' | 'lluvia' | 'tormenta') => {
    setLoadingClima(true)
    try {
      const data = await activarClima(tipo)
      setClimaActual(data.clima)
      setMensajeClima(data.mensaje)
      const nuevasAtracciones = await getAtracciones()
      setAtracciones(nuevasAtracciones)
    } catch (error) {
      console.error("Error cambiando clima:", error)
    } finally {
      setLoadingClima(false)
    }
  }

  const getEstadoBadge = (estado: Atraccion["estado"]) => {
    switch (estado) {
      case "ACTIVA":
        return <Badge className="bg-primary/20 text-primary border-primary/30"><CheckCircle2 className="w-3 h-3 mr-1" />Activa</Badge>
      case "CERRADA":
        return <Badge variant="destructive" className="bg-destructive/20 text-destructive border-destructive/30"><XCircle className="w-3 h-3 mr-1" />Cerrada</Badge>
      case "EN_MANTENIMIENTO":
        return <Badge className="bg-chart-3/20 text-chart-3 border-chart-3/30"><AlertTriangle className="w-3 h-3 mr-1" />Mantenimiento</Badge>
      default:
        return <Badge variant="secondary">Desconocido</Badge>
    }
  }

  const getTipoIcon = (tipo: string) => {
    switch (tipo) {
      case "MECANICA_ALTURA": return <Zap className="w-4 h-4 text-chart-4" />
      case "ACUATICA": return <Users className="w-4 h-4 text-chart-2" />
      case "INFANTIL": return <Ticket className="w-4 h-4 text-chart-5" />
      case "SIMULADOR": return <TrendingUp className="w-4 h-4 text-primary" />
      default: return <Ticket className="w-4 h-4 text-muted-foreground" />
    }
  }

  const getClimaIcon = () => {
    switch (climaActual) {
      case "LLUVIA": return <CloudRain className="w-5 h-5 text-blue-400" />
      case "TORMENTA": return <CloudLightning className="w-5 h-5 text-yellow-400" />
      default: return <Sun className="w-5 h-5 text-chart-3" />
    }
  }

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <div className="text-center">
          <Zap className="w-12 h-12 text-primary mx-auto animate-pulse" />
          <p className="mt-4 text-muted-foreground">Cargando TechPark...</p>
        </div>
      </div>
    )
  }

  if (!logueado) {
    return (
      <div
        className="min-h-screen flex items-center justify-center p-6 bg-cover bg-center bg-no-repeat"
        style={{ backgroundImage: "linear-gradient(rgba(0,0,0,0.45), rgba(0,0,0,0.45)), url('/parque.jpeg')" }}
      >
        <Card className="w-full max-w-md bg-white/10 backdrop-blur-md border-white/20">
          <CardHeader>
            <div className="flex justify-center mb-2">
              <div className="w-14 h-14 rounded-full bg-primary/20 flex items-center justify-center">
                <Zap className="w-8 h-8 text-primary" />
              </div>
            </div>
            <CardTitle className="text-2xl text-center text-white">TechPark UQ</CardTitle>
            <p className="text-center text-white/60">Ingrese al sistema</p>
          </CardHeader>
          <CardContent className="space-y-4">
            <input
              type="text"
              placeholder="Usuario"
              value={usuarioLogin}
              onChange={(e) => setUsuarioLogin(e.target.value)}
              className="w-full px-3 py-2 rounded-md border border-white/20 bg-white/10 text-white placeholder:text-white/40"
            />
            <select
              value={rolLogin}
              onChange={(e) => setRolLogin(e.target.value)}
              className="w-full px-3 py-2 rounded-md border border-white/20 bg-white/10 text-white"
            >
              <option value="visitante" className="text-black">Visitante</option>
              <option value="operador" className="text-black">Operador</option>
              <option value="admin" className="text-black">Administrador</option>
            </select>
            <Button className="w-full" onClick={iniciarSesion}>Iniciar Sesión</Button>
          </CardContent>
        </Card>
      </div>
    )
  }

  return (
    <div
      className="flex min-h-screen bg-cover bg-center bg-no-repeat"
      style={{ backgroundImage: "linear-gradient(rgba(0,0,0,0.45), rgba(0,0,0,0.45)), url('/parque.jpeg')" }}
    >
      {/* Sidebar — cristalino */}
      <aside className={cn(
        "fixed inset-y-0 left-0 z-50 w-64 bg-white/10 backdrop-blur-md border-r border-white/20 transform transition-transform duration-200 ease-in-out lg:relative lg:translate-x-0",
        sidebarOpen ? "translate-x-0" : "-translate-x-full"
      )}>
        <div className="flex flex-col h-full">
          <div className="flex items-center justify-between p-6 border-b border-white/20">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-lg bg-primary/20 flex items-center justify-center">
                <Zap className="w-6 h-6 text-primary" />
              </div>
              <div>
                <h1 className="font-bold text-lg text-white">TechPark</h1>
                <p className="text-xs text-primary">{usuarioLogin} • {rolLogin.toUpperCase()}</p>
                <p className="text-xs text-white/50">Quindío</p>
              </div>
            </div>
            <Button variant="ghost" size="icon" className="lg:hidden text-white" onClick={() => setSidebarOpen(false)}>
              <X className="w-5 h-5" />
            </Button>
          </div>

          <nav className="flex-1 p-4 space-y-1">
            {navItems
              .filter((item) => {
                if (rolLogin === "visitante") return item.id === "inicio" || item.id === "usuario" || item.id === "stats"
                if (rolLogin === "operador") return item.id === "inicio" || item.id === "stats"
                return true
              })
              .map((item) => {
                const Icon = item.icon
                return (
                  <button
                    key={item.id}
                    onClick={() => { setActivePanel(item.id); setSidebarOpen(false) }}
                    className={cn(
                      "w-full flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium transition-colors",
                      activePanel === item.id
                        ? "bg-white/20 text-white"
                        : "text-white/60 hover:bg-white/10 hover:text-white"
                    )}
                  >
                    <Icon className="w-5 h-5" />
                    {item.label}
                  </button>
                )
              })}
          </nav>

          <div className="p-4 border-t border-white/20 space-y-3">
            <div className="flex items-center gap-3 p-3 rounded-lg bg-white/10">
              {getClimaIcon()}
              <div>
                <p className="text-sm font-medium text-white">{climaActual}</p>
                <p className="text-xs text-white/50">Estado del parque</p>
              </div>
            </div>
            <Button
              variant="outline"
              className="w-full border-white/20 text-white hover:bg-white/10"
              onClick={() => { setLogueado(false); setUsuarioLogin(""); setRolLogin("visitante"); setActivePanel("inicio") }}
            >
              Cerrar Sesión
            </Button>
          </div>
        </div>
      </aside>

      {sidebarOpen && (
        <div className="fixed inset-0 bg-black/50 backdrop-blur-sm z-40 lg:hidden" onClick={() => setSidebarOpen(false)} />
      )}

      <main className="flex-1 lg:ml-0 overflow-auto">
        <header className="sticky top-0 z-30 flex items-center gap-4 px-4 py-3 bg-black/30 backdrop-blur border-b border-white/10 lg:hidden">
          <Button variant="ghost" size="icon" className="text-white" onClick={() => setSidebarOpen(true)}>
            <Menu className="w-5 h-5" />
          </Button>
          <h1 className="font-semibold text-white">TechPark UQ</h1>
        </header>

        <div className="p-6 lg:p-8">

          {/* ── PANEL INICIO ── */}
          {activePanel === "inicio" && (
            <div className="space-y-6">
              <div>
                <h2 className="text-2xl font-bold text-white">Visualización General del Parque</h2>
                <p className="text-white/60 mt-1">Monitoreo en tiempo real de zonas y disponibilidad de atracciones</p>
                <div className="flex gap-2 mt-4">
                  <input
                    type="text"
                    placeholder="Buscar atraccion por ID. Ej: A1"
                    value={busqueda}
                    onChange={(e) => setBusqueda(e.target.value)}
                    className="px-3 py-2 rounded-md border border-white/20 bg-white/10 text-white placeholder:text-white/40"
                  />
                  <Button onClick={buscarAtraccion}>Buscar</Button>
                </div>
                {resultadoBusqueda && (
                  <Card className="bg-white/10 backdrop-blur-md border-white/20 mt-4">
                    <CardContent className="p-4">
                      <p className="font-semibold text-white">Resultado de busqueda:</p>
                      {resultadoBusqueda.mensaje ? (
                        <p className="text-sm text-destructive">{resultadoBusqueda.mensaje}</p>
                      ) : (
                        <>
                          <p className="text-sm text-white/70">ID: {resultadoBusqueda.id}</p>
                          <p className="text-sm text-white/70">Nombre: {resultadoBusqueda.nombre}</p>
                          <p className="text-sm text-white/70">Tipo: {resultadoBusqueda.tipo}</p>
                          <p className="text-sm text-white/70">Capacidad: {resultadoBusqueda.capacidadMaxima}</p>
                          <p className="text-sm text-white/70">Estado: {resultadoBusqueda.estado}</p>
                          <p className="text-sm text-white/70">Tiempo espera: {resultadoBusqueda.tiempoEsperaMinutos} min</p>
                        </>
                      )}
                    </CardContent>
                  </Card>
                )}
              </div>

              <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                <Card className="bg-white/10 backdrop-blur-md border-white/20">
                  <CardContent className="p-4">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-lg bg-primary/20 flex items-center justify-center">
                        <CheckCircle2 className="w-5 h-5 text-primary" />
                      </div>
                      <div>
                        <p className="text-2xl font-bold text-white">{atracciones.filter(a => a.estado === "ACTIVA").length}</p>
                        <p className="text-xs text-white/60">Activas</p>
                      </div>
                    </div>
                  </CardContent>
                </Card>
                <Card className="bg-white/10 backdrop-blur-md border-white/20">
                  <CardContent className="p-4">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-lg bg-chart-3/20 flex items-center justify-center">
                        <AlertTriangle className="w-5 h-5 text-chart-3" />
                      </div>
                      <div>
                        <p className="text-2xl font-bold text-white">{atracciones.filter(a => a.estado === "EN_MANTENIMIENTO").length}</p>
                        <p className="text-xs text-white/60">Mantenimiento</p>
                      </div>
                    </div>
                  </CardContent>
                </Card>
                <Card className="bg-white/10 backdrop-blur-md border-white/20">
                  <CardContent className="p-4">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-lg bg-chart-2/20 flex items-center justify-center">
                        <Users className="w-5 h-5 text-chart-2" />
                      </div>
                      <div>
                        <p className="text-2xl font-bold text-white">{atracciones.reduce((acc, a) => acc + a.personasEnFila, 0)}</p>
                        <p className="text-xs text-white/60">En filas</p>
                      </div>
                    </div>
                  </CardContent>
                </Card>
                <Card className="bg-white/10 backdrop-blur-md border-white/20">
                  <CardContent className="p-4">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-lg bg-chart-4/20 flex items-center justify-center">
                        <Timer className="w-5 h-5 text-chart-4" />
                      </div>
                      <div>
                        <p className="text-2xl font-bold text-white">
                          {Math.round(
                            atracciones.filter(a => a.estado === "ACTIVA").reduce((acc, a) => acc + a.tiempoEsperaMinutos, 0) /
                            (atracciones.filter(a => a.estado === "ACTIVA").length || 1)
                          )}
                        </p>
                        <p className="text-xs text-white/60">Min promedio</p>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {atracciones.length === 0 ? (
                  <Card className="bg-white/10 backdrop-blur-md border-white/20 col-span-full">
                    <CardContent className="p-6 text-center text-destructive">
                      No se pudo conectar al backend.
                    </CardContent>
                  </Card>
                ) : (
                  atracciones.map((atraccion) => (
                    <Card key={atraccion.id} className={cn("bg-white/10 backdrop-blur-md border-white/20 transition-all hover:border-primary/50", atraccion.estado !== "ACTIVA" && "opacity-60")}>
                      <CardHeader className="pb-3">
                        <div className="flex items-start justify-between">
                          <div className="flex items-center gap-2">
                            {getTipoIcon(atraccion.tipo)}
                            <CardTitle className="text-base font-semibold text-white">{atraccion.nombre}</CardTitle>
                          </div>
                          {getEstadoBadge(atraccion.estado)}
                        </div>
                        <p className="text-sm text-white/50">{atraccion.tipo}</p>
                      </CardHeader>
                      <CardContent className="pt-0">
                        <div className="grid grid-cols-2 gap-4">
                          <div className="flex items-center gap-2">
                            <Clock className="w-4 h-4 text-white/50" />
                            <span className="text-sm text-white">{atraccion.estado === "ACTIVA" ? `${atraccion.tiempoEsperaMinutos} min` : "-"}</span>
                          </div>
                          <div className="flex items-center gap-2">
                            <Users className="w-4 h-4 text-white/50" />
                            <span className={cn("text-sm", atraccion.personasEnFila >= atraccion.capacidadMaxima ? "text-destructive font-bold" : "text-white")}>
                              {atraccion.personasEnFila} / {atraccion.capacidadMaxima}
                            </span>
                          </div>
                        </div>
                        {atraccion.estado === "ACTIVA" && atraccion.tiempoEsperaMinutos > 0 && (
                          <div className="mt-3">
                            <div className="h-1.5 bg-white/20 rounded-full overflow-hidden">
                              <div
                                className={cn("h-full rounded-full transition-all", atraccion.personasEnFila >= atraccion.capacidadMaxima ? "bg-destructive" : "bg-primary")}
                                style={{ width: `${Math.min((atraccion.personasEnFila / atraccion.capacidadMaxima) * 100, 100)}%` }}
                              />
                            </div>
                          </div>
                        )}
                        {atraccion.estado === "ACTIVA" && atraccion.personasEnFila < atraccion.capacidadMaxima && (
                          <Button className="mt-4 w-full" onClick={() => manejarFila(String(atraccion.id))}>
                            Unirse a la fila
                          </Button>
                        )}
                        {atraccion.estado === "ACTIVA" && atraccion.personasEnFila >= atraccion.capacidadMaxima && (
                          <Button className="mt-4 w-full" disabled variant="outline">
                            Fila llena
                          </Button>
                        )}
                      </CardContent>
                    </Card>
                  ))
                )}
              </div>
            </div>
          )}

          {/* ── PANEL USUARIO ── */}
          {activePanel === "usuario" && (
            <div className="space-y-6">
              <div>
                <h2 className="text-2xl font-bold text-white">Panel del Visitante</h2>
                <p className="text-white/60 mt-1">Estado actual de las atracciones y tiempos de espera</p>
              </div>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {atracciones.map((atraccion) => (
                  <Card key={atraccion.id} className={cn("bg-white/10 backdrop-blur-md border-white/20", atraccion.estado !== "ACTIVA" && "opacity-50")}>
                    <CardHeader className="pb-2">
                      <div className="flex items-center justify-between">
                        <CardTitle className="text-sm font-semibold text-white">{atraccion.nombre}</CardTitle>
                        {getEstadoBadge(atraccion.estado)}
                      </div>
                    </CardHeader>
                    <CardContent className="pt-0 space-y-2">
                      <div className="flex items-center gap-2 text-sm text-white/60">
                        <Clock className="w-4 h-4" />
                        <span>Espera: {atraccion.estado === "ACTIVA" ? `${atraccion.tiempoEsperaMinutos} min` : "No disponible"}</span>
                      </div>
                      <div className="flex items-center gap-2 text-sm text-white/60">
                        <Users className="w-4 h-4" />
                        <span className={cn(atraccion.personasEnFila >= atraccion.capacidadMaxima ? "text-destructive font-bold" : "")}>
                          En fila: {atraccion.personasEnFila} / {atraccion.capacidadMaxima}
                        </span>
                      </div>
                      {atraccion.estado === "ACTIVA" && atraccion.personasEnFila < atraccion.capacidadMaxima && (
                        <Button size="sm" className="w-full mt-2" onClick={() => manejarFila(String(atraccion.id))}>
                          Unirse a la fila
                        </Button>
                      )}
                      {atraccion.estado === "ACTIVA" && atraccion.personasEnFila >= atraccion.capacidadMaxima && (
                        <Button size="sm" className="w-full mt-2" disabled variant="outline">
                          Fila llena
                        </Button>
                      )}
                    </CardContent>
                  </Card>
                ))}
              </div>

              <Card className="bg-white/10 backdrop-blur-md border-white/20">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2 text-white">
                    <Navigation className="w-5 h-5 text-primary" />
                    Ruta Sugerida
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="space-y-3">
                    {atracciones.filter(a => a.estado === "ACTIVA").slice(0, 4).map((a, index) => (
                      <div key={a.id} className="flex items-center gap-3">
                        <div className={cn(
                          "w-8 h-8 rounded-full flex items-center justify-center text-sm font-medium",
                          index === 0 ? "bg-primary text-primary-foreground" : "bg-white/20 text-white"
                        )}>
                          {index + 1}
                        </div>
                        <span className={cn("text-sm", index === 0 ? "font-medium text-white" : "text-white/60")}>{a.nombre}</span>
                        {index === 0 && <Badge className="ml-auto bg-primary/20 text-primary">Menor espera</Badge>}
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>
            </div>
          )}

          {/* ── PANEL ADMIN ── */}
          {activePanel === "admin" && (
            <div className="space-y-6">
              <div>
                <h2 className="text-2xl font-bold text-white">Panel de Administración</h2>
                <p className="text-white/60 mt-1">Gestión de zonas y control de contingencias climáticas</p>
              </div>

              <div>
                <h3 className="text-lg font-semibold text-white mb-3">Zonas del Parque</h3>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                  {zonas.map((zona) => (
                    <Card key={zona.id} className="bg-white/10 backdrop-blur-md border-white/20">
                      <CardContent className="p-4">
                        <div className="w-10 h-10 rounded-lg bg-primary/20 flex items-center justify-center mb-3">
                          <Map className="w-5 h-5 text-primary" />
                        </div>
                        <h4 className="font-semibold text-white">{zona.nombre}</h4>
                        <p className="text-sm text-white/60 mt-1">Capacidad: {zona.capacidad}</p>
                        <Badge className="mt-2 bg-primary/20 text-primary">{zona.id}</Badge>
                      </CardContent>
                    </Card>
                  ))}
                </div>
              </div>

              <div>
                <h3 className="text-lg font-semibold text-white mb-3">Control Climático</h3>
                <Card className="bg-white/10 backdrop-blur-md border-white/20">
                  <CardContent className="p-6">
                    <div className="flex items-center gap-3 mb-4 p-3 rounded-lg bg-white/10">
                      {getClimaIcon()}
                      <div>
                        <p className="font-medium text-white">Clima actual: {climaActual}</p>
                        <p className="text-sm text-white/60">{mensajeClima}</p>
                      </div>
                    </div>
                    <div className="flex gap-3 flex-wrap">
                      <Button onClick={() => cambiarClima('soleado')} disabled={loadingClima} className="bg-yellow-500/20 text-yellow-400 hover:bg-yellow-500/30 border border-yellow-500/30" variant="outline">
                        <Sun className="w-4 h-4 mr-2" /> Soleado
                      </Button>
                      <Button onClick={() => cambiarClima('lluvia')} disabled={loadingClima} className="bg-blue-500/20 text-blue-400 hover:bg-blue-500/30 border border-blue-500/30" variant="outline">
                        <CloudRain className="w-4 h-4 mr-2" /> Lluvia
                      </Button>
                      <Button onClick={() => cambiarClima('tormenta')} disabled={loadingClima} className="bg-destructive/20 text-destructive hover:bg-destructive/30 border border-destructive/30" variant="outline">
                        <CloudLightning className="w-4 h-4 mr-2" /> Tormenta
                      </Button>
                    </div>
                  </CardContent>
                </Card>
              </div>
            </div>
          )}

          {/* ── PANEL STATS ── */}
          {activePanel === "stats" && (
            <div className="space-y-6">
              <div>
                <h2 className="text-2xl font-bold text-white">Estadísticas e Informes</h2>
                <p className="text-white/60 mt-1">Datos en tiempo real del parque</p>
              </div>

              {estadisticas && (
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                  <Card className="bg-white/10 backdrop-blur-md border-white/20">
                    <CardContent className="p-4 text-center">
                      <p className="text-3xl font-bold text-white">{estadisticas.totalAtracciones}</p>
                      <p className="text-sm text-white/60 mt-1">Total Atracciones</p>
                    </CardContent>
                  </Card>
                  <Card className="bg-white/10 backdrop-blur-md border-white/20">
                    <CardContent className="p-4 text-center">
                      <p className="text-3xl font-bold text-primary">{estadisticas.atraccionesActivas}</p>
                      <p className="text-sm text-white/60 mt-1">Activas</p>
                    </CardContent>
                  </Card>
                  <Card className="bg-white/10 backdrop-blur-md border-white/20">
                    <CardContent className="p-4 text-center">
                      <p className="text-3xl font-bold text-white">{estadisticas.totalPersonasEnFila}</p>
                      <p className="text-sm text-white/60 mt-1">En Filas</p>
                    </CardContent>
                  </Card>
                  <Card className="bg-white/10 backdrop-blur-md border-white/20">
                    <CardContent className="p-4 text-center">
                      <p className="text-3xl font-bold text-chart-3">{estadisticas.tiempoPromedioEspera} min</p>
                      <p className="text-sm text-white/60 mt-1">Tiempo Promedio</p>
                    </CardContent>
                  </Card>
                </div>
              )}

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <Card className="bg-white/10 backdrop-blur-md border-white/20">
                  <CardHeader>
                    <CardTitle className="text-white">Atracciones por Estado</CardTitle>
                  </CardHeader>
                  <CardContent className="space-y-3">
                    {["ACTIVA", "EN_MANTENIMIENTO", "CERRADA"].map((estado) => {
                      const count = atracciones.filter(a => a.estado === estado).length
                      const label = estado === "ACTIVA" ? "Activas" : estado === "EN_MANTENIMIENTO" ? "Mantenimiento" : "Cerradas"
                      const color = estado === "ACTIVA" ? "bg-primary" : estado === "EN_MANTENIMIENTO" ? "bg-chart-3" : "bg-destructive"
                      return (
                        <div key={estado} className="flex items-center gap-3">
                          <span className="text-sm text-white/60 w-28">{label}</span>
                          <div className="flex-1 h-2 bg-white/20 rounded-full overflow-hidden">
                            <div className={`h-full ${color} rounded-full`} style={{ width: `${(count / (atracciones.length || 1)) * 100}%` }} />
                          </div>
                          <span className="text-sm font-medium text-white w-4">{count}</span>
                        </div>
                      )
                    })}
                  </CardContent>
                </Card>

                <Card className="bg-white/10 backdrop-blur-md border-white/20">
                  <CardHeader>
                    <CardTitle className="text-white">Tiempos de Espera</CardTitle>
                  </CardHeader>
                  <CardContent className="space-y-3">
                    {atracciones.filter(a => a.estado === "ACTIVA" && a.tiempoEsperaMinutos > 0)
                      .sort((a, b) => b.tiempoEsperaMinutos - a.tiempoEsperaMinutos)
                      .map((a) => (
                        <div key={a.id} className="flex items-center gap-3">
                          <span className="text-sm text-white/60 w-36 truncate">{a.nombre}</span>
                          <div className="flex-1 h-2 bg-white/20 rounded-full overflow-hidden">
                            <div className="h-full bg-primary rounded-full" style={{ width: `${(a.tiempoEsperaMinutos / 60) * 100}%` }} />
                          </div>
                          <span className="text-sm font-medium text-white w-12 text-right">{a.tiempoEsperaMinutos} min</span>
                        </div>
                      ))}
                  </CardContent>
                </Card>
              </div>
            </div>
          )}

        </div>
      </main>
    </div>
  )
}