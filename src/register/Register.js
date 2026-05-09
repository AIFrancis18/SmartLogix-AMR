import { useState } from "react";
import "./Register.css";

function Register() {

  const [nombre, setNombre] = useState("");
  const [correo, setCorreo] = useState("");
  const [contrasena, setContrasena] = useState("");
  const [rol, setRol] = useState("OPERADOR");

  const [mensaje, setMensaje] = useState("");
  const [loading, setLoading] = useState(false);

  // 🔥 REGISTRAR USUARIO
  const registrar = async () => {

    try {

      setMensaje("");
      setLoading(true);

      const correoLimpio = correo.trim().toLowerCase();

      // 🔥 VALIDACIONES
      const regexCorreo = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

      const dominiosPermitidos = [
        "gmail.com",
        "hotmail.com",
        "duoc.cl"
      ];

      if (!nombre.trim()) {
        throw new Error("El nombre es obligatorio");
      }

      if (nombre.trim().length < 3) {
        throw new Error(
          "El nombre debe tener mínimo 3 caracteres"
        );
      }

      if (!regexCorreo.test(correoLimpio)) {
        throw new Error("Ingrese un correo válido");
      }

      const dominio = correoLimpio.split("@")[1];

      if (!dominiosPermitidos.includes(dominio)) {
        throw new Error(
          "Solo se permiten correos Gmail, Hotmail o Duoc"
        );
      }

      if (
        contrasena.length < 4 ||
        contrasena.length > 12
      ) {
        throw new Error(
          "La contraseña debe tener entre 4 y 12 caracteres"
        );
      }

      // 🔥 PETICIÓN
      const response = await fetch(
        "http://localhost:9090/usuarios",
        {
          method: "POST",

          headers: {
            "Content-Type": "application/json"
          },

          body: JSON.stringify({
            nombre: nombre.trim(),
            correo: correoLimpio,
            contrasena,
            rol
          })
        }
      );

      if (!response.ok) {
        throw new Error(
          "Error al registrar usuario"
        );
      }

      setMensaje(
        "✅ Usuario creado correctamente"
      );

      // 🔥 LIMPIAR FORM
      setNombre("");
      setCorreo("");
      setContrasena("");
      setRol("OPERADOR");

      // 🔥 REDIRECCIÓN
      setTimeout(() => {

        window.location.href = "/";

      }, 1400);

    } catch (error) {

      setMensaje("❌ " + error.message);

    } finally {

      setLoading(false);

    }
  };

  return (

    <div className="container">

      <div className="card">

        <h2>🚀 Crear Cuenta</h2>

        <p>
          Registra un nuevo usuario en SmartLogix
        </p>

        {/* 🔥 NOMBRE */}
        <input
          className="input"
          type="text"
          placeholder="Nombre completo"
          value={nombre}
          onChange={(e) =>
            setNombre(e.target.value)
          }
        />

        {/* 🔥 CORREO */}
        <input
          className="input"
          type="email"
          placeholder="Correo electrónico"
          value={correo}
          onChange={(e) =>
            setCorreo(e.target.value)
          }
        />

        {/* 🔥 CONTRASEÑA */}
        <input
          className="input"
          type="password"
          placeholder="Contraseña"
          value={contrasena}
          onChange={(e) =>
            setContrasena(e.target.value)
          }
        />

        {/* 🔥 ROL */}
        <select
          className="input"
          value={rol}
          onChange={(e) =>
            setRol(e.target.value)
          }
        >
          <option value="ADMIN">
            👑 ADMIN
          </option>

          <option value="OPERADOR">
            📦 OPERADOR
          </option>

          <option value="LOGISTICA">
            🚚 LOGÍSTICA
          </option>

        </select>

        {/* 🔥 BOTÓN */}
        <button
          className="button"
          onClick={registrar}
          disabled={
            loading ||
            !nombre ||
            !correo ||
            !contrasena
          }
        >

          {loading
            ? "Registrando..."
            : "Registrarse"}

        </button>

        {/* 🔥 MENSAJE */}
        {mensaje && (
          <p className="message">
            {mensaje}
          </p>
        )}

        {/* 🔥 LINK LOGIN */}
        <p
          className="link"
          onClick={() =>
            (window.location.href = "/")
          }
        >
          ¿Ya tienes cuenta? Inicia sesión
        </p>

      </div>

    </div>
  );
}

export default Register;