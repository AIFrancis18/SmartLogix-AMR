import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

// 🔥 IMPORTS LOGIN / REGISTER
import Login from "./login/Login";
import Register from "./register/Register";

// 🔥 IMPORTS PAGES
import AdminPage from "./pages/AdminPage";
import OperadorPage from "./pages/OperadorPage";
import LogisticaPage from "./pages/LogisticaPage";
import DashboardPage from "./dashboard/DashboardPage";

// 🔥 PROTECCIÓN POR ROL
function RutaProtegida({ children, rolPermitido }) {

  const token = localStorage.getItem("token");

  if (!token) {
    return <Navigate to="/" />;
  }

  try {

    const data = jwtDecode(token);

    // 🔥 VALIDAR ROL
    if (rolPermitido && data.rol !== rolPermitido) {
      return <Navigate to="/" />;
    }

    return children;

  } catch {

    localStorage.removeItem("token");

    return <Navigate to="/" />;
  }
}

function App() {

  return (

    <BrowserRouter>

      <Routes>

        {/* 🔓 RUTAS PÚBLICAS */}
        <Route path="/" element={<Login />} />

        <Route
          path="/register"
          element={<Register />}
        />

        {/* 🔐 ADMIN */}
        <Route
          path="/admin"
          element={
            <RutaProtegida rolPermitido="ADMIN">
              <AdminPage />
            </RutaProtegida>
          }
        />

        {/* 🔥 DASHBOARD ADMIN */}
        <Route
          path="/dashboard"
          element={
            <RutaProtegida rolPermitido="ADMIN">
              <DashboardPage />
            </RutaProtegida>
          }
        />

        {/* 🔐 OPERADOR */}
        <Route
          path="/operador"
          element={
            <RutaProtegida rolPermitido="OPERADOR">
              <OperadorPage />
            </RutaProtegida>
          }
        />

        {/* 🔐 LOGÍSTICA */}
        <Route
          path="/logistica"
          element={
            <RutaProtegida rolPermitido="LOGISTICA">
              <LogisticaPage />
            </RutaProtegida>
          }
        />

        {/* 🔥 CUALQUIER OTRA RUTA */}
        <Route
          path="*"
          element={<Navigate to="/" />}
        />

      </Routes>

    </BrowserRouter>
  );
}

export default App;