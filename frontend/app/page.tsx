"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import {
  Map,
  User,
  Settings,
  BarChart3,
  Clock,
  Users,
  AlertTriangle,
  CheckCircle2,
  XCircle,
  Zap,
  Timer,
  TrendingUp,
  Ticket,
  Navigation,
  CloudSun,
  Menu,
  X,
} from "lucide-react"
import { cn } from "@/lib/utils"
import { getAtracciones, type Atraccion } from "@/lib/api"

// Datos de ejemplo (fallback si la API no está disponible)
const atraccionesEjemplo: Atraccion[] = [
  { id: 1, nombre: "Montaña Rusa Tech", tipo: "Extrema", estado: "ACTIVA", tiempoEsperaMinutos: 25, capacidadMaxima: 24, personasEnFila: 120 },
  { id: 2, nombre: "Río Virtual", tipo: "Familiar", estado: "ACTIVA", tiempoEsperaMinutos: 15, capacidadMaxima: 40, personasEnFila: 80 },
  { id: 3, nombre: "Torre Digital", tipo: "Extrema", estado: "MANTENIMIENTO", tiempoEsperaMinutos: 0, capacidadMaxima: 16, personasEnFila: 0 },
  { id: 4, nombre: "Carrusel Cyber", tipo: "Infantil", estado: "ACTIVA", tiempoEsperaMinutos: 5, capacidadMaxima: 32, personasEnFila: 25 },
  { id: 5, nombre: "Simulador 4D", tipo: "Tecnológica", estado: "ACTIVA", tiempoEsperaMinutos: 35, capacidadMaxima: 20, personasEnFila: 95 },
  { id: 6, nombre: "Casa del Terror VR", tipo: "Temática", estado: "CERRADA", tiempoEsperaMinutos: 0, capacidadMaxima: 12, personasEnFila: 0 },
]

const navItems = [
  { id: "inicio", label: "Inicio / Mapa", icon: Map },
  { id: "usuario", label: "Panel Usuario", icon: User },
  { id: "admin", label: "Administración", icon: Settings },
  { id: "stats", label: "Estadísticas", icon: BarChart3 },
]

export default function TechParkDashboard() {
  const [activePanel, setActivePanel] = useState("inicio")
  const [atracciones, setAtracciones] = useState<Atraccion[]>(atraccionesEjemplo)
  const [loading, setLoading] = useState(false)
  const [sidebarOpen, setSidebarOpen] = useState(false)

  // Cargar atracciones desde la API de Spring Boot
  useEffect(() => {
    const cargarAtracciones = async () => {
      setLoading(true)
      try {
        const data = await getAtracciones()
        setAtracciones(data)
      } catch (error) {
        console.error("Error cargando datos de la API:", error)
        // Usar datos de ejemplo como fallback
        setAtracciones(atraccionesEjemplo)
      } finally {
        setLoading(false)
      }
    }
    cargarAtracciones()
  }, [])

  const getEstadoBadge = (estado: Atraccion["estado"]) => {
    switch (estado) {
      case "ACTIVA":
        return <Badge className="bg-primary/20 text-primary border-primary/30"><CheckCircle2 className="w-3 h-3 mr-1" />Activa</Badge>
      case "CERRADA":
        return <Badge variant="destructive" className="bg-destructive/20 text-destructive border-destructive/30"><XCircle className="w-3 h-3 mr-1" />Cerrada</Badge>
      case "MANTENIMIENTO":
        return <Badge className="bg-chart-3/20 text-chart-3 border-chart-3/30"><AlertTriangle className="w-3 h-3 mr-1" />Mantenimiento</Badge>
      default:
        return <Badge variant="secondary">Desconocido</Badge>
    }
  }

  const getTipoIcon = (tipo: string) => {
    switch (tipo) {
      case "Extrema": return <Zap className="w-4 h-4 text-chart-4" />
      case "Familiar": return <Users className="w-4 h-4 text-chart-2" />
      case "Infantil": return <Ticket className="w-4 h-4 text-chart-5" />
      case "Tecnológica": return <TrendingUp className="w-4 h-4 text-primary" />
      default: return <Ticket className="w-4 h-4 text-muted-foreground" />
    }
  }

  return (
    <div className="flex min-h-screen">
      {/* Sidebar */}
      <aside className={cn(
        "fixed inset-y-0 left-0 z-50 w-64 bg-sidebar border-r border-sidebar-border transform transition-transform duration-200 ease-in-out lg:relative lg:translate-x-0",
        sidebarOpen ? "translate-x-0" : "-translate-x-full"
      )}>
        <div className="flex flex-col h-full">
          <div className="flex items-center justify-between p-6 border-b border-sidebar-border">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-lg bg-primary/20 flex items-center justify-center">
                <Zap className="w-6 h-6 text-primary" />
              </div>
              <div>
                <h1 className="font-bold text-lg text-sidebar-foreground">TechPark</h1>
                <p className="text-xs text-muted-foreground">Quindío</p>
              </div>
            </div>
            <Button
              variant="ghost"
              size="icon"
              className="lg:hidden"
              onClick={() => setSidebarOpen(false)}
            >
              <X className="w-5 h-5" />
            </Button>
          </div>
          
          <nav className="flex-1 p-4 space-y-1">
            {navItems.map((item) => {
              const Icon = item.icon
              return (
                <button
                  key={item.id}
                  onClick={() => {
                    setActivePanel(item.id)
                    setSidebarOpen(false)
                  }}
                  className={cn(
                    "w-full flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium transition-colors",
                    activePanel === item.id
                      ? "bg-sidebar-accent text-primary"
                      : "text-muted-foreground hover:bg-sidebar-accent/50 hover:text-sidebar-foreground"
                  )}
                >
                  <Icon className="w-5 h-5" />
                  {item.label}
                </button>
              )
            })}
          </nav>

          <div className="p-4 border-t border-sidebar-border">
            <div className="flex items-center gap-3 p-3 rounded-lg bg-sidebar-accent/50">
              <CloudSun className="w-5 h-5 text-chart-3" />
              <div>
                <p className="text-sm font-medium text-sidebar-foreground">28°C Soleado</p>
                <p className="text-xs text-muted-foreground">Clima ideal</p>
              </div>
            </div>
          </div>
        </div>
      </aside>

      {/* Overlay móvil */}
      {sidebarOpen && (
        <div 
          className="fixed inset-0 bg-background/80 backdrop-blur-sm z-40 lg:hidden"
          onClick={() => setSidebarOpen(false)}
        />
      )}

      {/* Contenido principal */}
      <main className="flex-1 lg:ml-0">
        {/* Header móvil */}
        <header className="sticky top-0 z-30 flex items-center gap-4 px-4 py-3 bg-background/95 backdrop-blur border-b border-border lg:hidden">
          <Button
            variant="ghost"
            size="icon"
            onClick={() => setSidebarOpen(true)}
          >
            <Menu className="w-5 h-5" />
          </Button>
          <h1 className="font-semibold">TechPark UQ</h1>
        </header>

        <div className="p-6 lg:p-8">
          {/* Panel Inicio */}
          {activePanel === "inicio" && (
            <div className="space-y-6">
              <div>
                <h2 className="text-2xl font-bold text-foreground">Visualización General del Parque</h2>
                <p className="text-muted-foreground mt-1">Monitoreo en tiempo real de zonas y disponibilidad de atracciones</p>
              </div>

              {/* Stats rápidas */}
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                <Card className="bg-card border-border">
                  <CardContent className="p-4">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-lg bg-primary/20 flex items-center justify-center">
                        <CheckCircle2 className="w-5 h-5 text-primary" />
                      </div>
                      <div>
                        <p className="text-2xl font-bold text-foreground">{atracciones.filter(a => a.estado === "ACTIVA").length}</p>
                        <p className="text-xs text-muted-foreground">Activas</p>
                      </div>
                    </div>
                  </CardContent>
                </Card>
                <Card className="bg-card border-border">
                  <CardContent className="p-4">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-lg bg-chart-3/20 flex items-center justify-center">
                        <AlertTriangle className="w-5 h-5 text-chart-3" />
                      </div>
                      <div>
                        <p className="text-2xl font-bold text-foreground">{atracciones.filter(a => a.estado === "MANTENIMIENTO").length}</p>
                        <p className="text-xs text-muted-foreground">Mantenimiento</p>
                      </div>
                    </div>
                  </CardContent>
                </Card>
                <Card className="bg-card border-border">
                  <CardContent className="p-4">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-lg bg-chart-2/20 flex items-center justify-center">
                        <Users className="w-5 h-5 text-chart-2" />
                      </div>
                      <div>
                        <p className="text-2xl font-bold text-foreground">{atracciones.reduce((acc, a) => acc + a.personasEnFila, 0)}</p>
                        <p className="text-xs text-muted-foreground">En filas</p>
                      </div>
                    </div>
                  </CardContent>
                </Card>
                <Card className="bg-card border-border">
                  <CardContent className="p-4">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-lg bg-chart-4/20 flex items-center justify-center">
                        <Timer className="w-5 h-5 text-chart-4" />
                      </div>
                      <div>
                        <p className="text-2xl font-bold text-foreground">{Math.round(atracciones.filter(a => a.estado === "ACTIVA").reduce((acc, a) => acc + a.tiempoEsperaMinutos, 0) / atracciones.filter(a => a.estado === "ACTIVA").length || 0)}</p>
                        <p className="text-xs text-muted-foreground">Min promedio</p>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              </div>

              {/* Grid de atracciones */}
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {loading ? (
                  <Card className="bg-card border-border col-span-full">
                    <CardContent className="p-6 text-center text-muted-foreground">
                      Cargando atracciones...
                    </CardContent>
                  </Card>
                ) : (
                  atracciones.map((atraccion) => (
                    <Card 
                      key={atraccion.id} 
                      className={cn(
                        "bg-card border-border transition-all hover:border-primary/50",
                        atraccion.estado !== "ACTIVA" && "opacity-60"
                      )}
                    >
                      <CardHeader className="pb-3">
                        <div className="flex items-start justify-between">
                          <div className="flex items-center gap-2">
                            {getTipoIcon(atraccion.tipo)}
                            <CardTitle className="text-base font-semibold text-foreground">{atraccion.nombre}</CardTitle>
                          </div>
                          {getEstadoBadge(atraccion.estado)}
                        </div>
                        <p className="text-sm text-muted-foreground">{atraccion.tipo}</p>
                      </CardHeader>
                      <CardContent className="pt-0">
                        <div className="grid grid-cols-2 gap-4">
                          <div className="flex items-center gap-2">
                            <Clock className="w-4 h-4 text-muted-foreground" />
                            <span className="text-sm text-foreground">
                              {atraccion.estado === "ACTIVA" ? `${atraccion.tiempoEsperaMinutos} min` : "-"}
                            </span>
                          </div>
                          <div className="flex items-center gap-2">
                            <Users className="w-4 h-4 text-muted-foreground" />
                            <span className="text-sm text-foreground">{atraccion.personasEnFila} / {atraccion.capacidadMaxima}</span>
                          </div>
                        </div>
{atraccion.estado === "ACTIVA" && atraccion.tiempoEsperaMinutos > 0 && (
                                          <div className="mt-3">
                                            <div className="h-1.5 bg-secondary rounded-full overflow-hidden">
                                              <div 
                                                className="h-full bg-primary rounded-full transition-all"
                                                style={{ width: `${Math.min((atraccion.personasEnFila / atraccion.capacidadMaxima) * 100, 100)}%` }}
                              />
                            </div>
                          </div>
                        )}
                      </CardContent>
                    </Card>
                  ))
                )}
              </div>
            </div>
          )}

          {/* Panel Usuario */}
          {activePanel === "usuario" && (
            <div className="space-y-6">
              <div>
                <h2 className="text-2xl font-bold text-foreground">Panel del Visitante</h2>
                <p className="text-muted-foreground mt-1">Consulta tu estado en la fila virtual y la ruta óptima sugerida</p>
              </div>

              <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                <Card className="bg-card border-border">
                  <CardHeader>
                    <CardTitle className="flex items-center gap-2 text-foreground">
                      <Ticket className="w-5 h-5 text-primary" />
                      Tu Fila Virtual
                    </CardTitle>
                  </CardHeader>
                  <CardContent className="space-y-4">
                    <div className="p-4 rounded-lg bg-primary/10 border border-primary/20">
                      <div className="flex items-center justify-between mb-2">
                        <span className="font-medium text-foreground">Montaña Rusa Tech</span>
                        <Badge className="bg-primary/20 text-primary">En espera</Badge>
                      </div>
                      <div className="flex items-center gap-4 text-sm text-muted-foreground">
                        <span className="flex items-center gap-1"><Users className="w-4 h-4" /> Posición: 15</span>
                        <span className="flex items-center gap-1"><Clock className="w-4 h-4" /> ~18 min</span>
                      </div>
                    </div>
                    <div className="p-4 rounded-lg bg-secondary border border-border">
                      <div className="flex items-center justify-between mb-2">
                        <span className="font-medium text-foreground">Simulador 4D</span>
                        <Badge variant="secondary">Próxima</Badge>
                      </div>
                      <div className="flex items-center gap-4 text-sm text-muted-foreground">
                        <span className="flex items-center gap-1"><Users className="w-4 h-4" /> Posición: 32</span>
                        <span className="flex items-center gap-1"><Clock className="w-4 h-4" /> ~45 min</span>
                      </div>
                    </div>
                  </CardContent>
                </Card>

                <Card className="bg-card border-border">
                  <CardHeader>
                    <CardTitle className="flex items-center gap-2 text-foreground">
                      <Navigation className="w-5 h-5 text-primary" />
                      Ruta Sugerida
                    </CardTitle>
                  </CardHeader>
                  <CardContent>
                    <div className="space-y-3">
                      {["Carrusel Cyber", "Río Virtual", "Simulador 4D", "Montaña Rusa Tech"].map((lugar, index) => (
                        <div key={lugar} className="flex items-center gap-3">
                          <div className={cn(
                            "w-8 h-8 rounded-full flex items-center justify-center text-sm font-medium",
                            index === 0 ? "bg-primary text-primary-foreground" : "bg-secondary text-secondary-foreground"
                          )}>
                            {index + 1}
                          </div>
                          <span className={cn(
                            "text-sm",
                            index === 0 ? "font-medium text-foreground" : "text-muted-foreground"
                          )}>{lugar}</span>
                          {index === 0 && <Badge className="ml-auto bg-primary/20 text-primary">Actual</Badge>}
                        </div>
                      ))}
                    </div>
                  </CardContent>
                </Card>
              </div>
            </div>
          )}

          {/* Panel Admin */}
          {activePanel === "admin" && (
            <div className="space-y-6">
              <div>
                <h2 className="text-2xl font-bold text-foreground">Panel de Administración</h2>
                <p className="text-muted-foreground mt-1">Gestión de personal, contingencias climáticas y carga de datos iniciales</p>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                <Card className="bg-card border-border hover:border-primary/50 transition-colors cursor-pointer">
                  <CardContent className="p-6">
                    <div className="w-12 h-12 rounded-lg bg-primary/20 flex items-center justify-center mb-4">
                      <Users className="w-6 h-6 text-primary" />
                    </div>
                    <h3 className="font-semibold text-foreground mb-1">Gestión de Personal</h3>
                    <p className="text-sm text-muted-foreground">Asignar operadores y supervisores a zonas</p>
                  </CardContent>
                </Card>
                <Card className="bg-card border-border hover:border-primary/50 transition-colors cursor-pointer">
                  <CardContent className="p-6">
                    <div className="w-12 h-12 rounded-lg bg-chart-3/20 flex items-center justify-center mb-4">
                      <CloudSun className="w-6 h-6 text-chart-3" />
                    </div>
                    <h3 className="font-semibold text-foreground mb-1">Contingencias Climáticas</h3>
                    <p className="text-sm text-muted-foreground">Protocolos de seguridad por clima</p>
                  </CardContent>
                </Card>
                <Card className="bg-card border-border hover:border-primary/50 transition-colors cursor-pointer">
                  <CardContent className="p-6">
                    <div className="w-12 h-12 rounded-lg bg-chart-2/20 flex items-center justify-center mb-4">
                      <Settings className="w-6 h-6 text-chart-2" />
                    </div>
                    <h3 className="font-semibold text-foreground mb-1">Configuración</h3>
                    <p className="text-sm text-muted-foreground">Carga de datos y configuración del sistema</p>
                  </CardContent>
                </Card>
              </div>
            </div>
          )}

          {/* Panel Stats */}
          {activePanel === "stats" && (
            <div className="space-y-6">
              <div>
                <h2 className="text-2xl font-bold text-foreground">Estadísticas e Informes</h2>
                <p className="text-muted-foreground mt-1">Análisis de afluencia, ingresos y clústeres del grafo</p>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <Card className="bg-card border-border">
                  <CardHeader>
                    <CardTitle className="text-foreground">Afluencia por Hora</CardTitle>
                  </CardHeader>
                  <CardContent>
                    <div className="space-y-3">
                      {[
                        { hora: "10:00", valor: 45 },
                        { hora: "12:00", valor: 78 },
                        { hora: "14:00", valor: 92 },
                        { hora: "16:00", valor: 85 },
                        { hora: "18:00", valor: 65 },
                      ].map((item) => (
                        <div key={item.hora} className="flex items-center gap-3">
                          <span className="text-sm text-muted-foreground w-12">{item.hora}</span>
                          <div className="flex-1 h-2 bg-secondary rounded-full overflow-hidden">
                            <div 
                              className="h-full bg-primary rounded-full"
                              style={{ width: `${item.valor}%` }}
                            />
                          </div>
                          <span className="text-sm font-medium text-foreground w-10 text-right">{item.valor}%</span>
                        </div>
                      ))}
                    </div>
                  </CardContent>
                </Card>

                <Card className="bg-card border-border">
                  <CardHeader>
                    <CardTitle className="text-foreground">Métricas del Día</CardTitle>
                  </CardHeader>
                  <CardContent className="space-y-4">
                    <div className="flex items-center justify-between p-3 rounded-lg bg-secondary">
                      <span className="text-muted-foreground">Visitantes totales</span>
                      <span className="font-bold text-foreground">2,847</span>
                    </div>
                    <div className="flex items-center justify-between p-3 rounded-lg bg-secondary">
                      <span className="text-muted-foreground">Tiempo promedio visita</span>
                      <span className="font-bold text-foreground">4.2 hrs</span>
                    </div>
                    <div className="flex items-center justify-between p-3 rounded-lg bg-secondary">
                      <span className="text-muted-foreground">Satisfacción</span>
                      <span className="font-bold text-primary">94%</span>
                    </div>
                    <div className="flex items-center justify-between p-3 rounded-lg bg-secondary">
                      <span className="text-muted-foreground">Ingresos del día</span>
                      <span className="font-bold text-foreground">$42.5M COP</span>
                    </div>
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
