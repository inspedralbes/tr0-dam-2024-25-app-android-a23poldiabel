const express = require('express');
const fs = require('fs');
const bodyParser = require('body-parser');
const app = express();
const port = 3000;

// Middleware para analizar JSON
app.use(bodyParser.json());

// Servir archivos estáticos (para que Vue.js pueda cargar el frontend)
app.use(express.static(__dirname + '/public'));

// Ruta para obtener todas las preguntas
app.get('/api/preguntas', (req, res) => {
    fs.readFile('test.json', 'utf8', (err, data) => {
        if (err) {
            return res.status(500).json({ message: 'Error al leer el archivo JSON.' });
        }

        let jsonData;
        try {
            jsonData = JSON.parse(data);
        } catch (parseError) {
            return res.status(500).json({ message: 'Error al analizar el archivo JSON.' });
        }

        res.json(jsonData);
    });
});

// Modificación en el método agregarPregunta
agregarPregunta() {
    if (this.nuevaPregunta.pregunta && this.nuevaPregunta.imatge && this.nuevaPregunta.respostes && this.nuevaPregunta.resposta_correcta !== null) {
        const nuevaPregunta = {
            pregunta: this.nuevaPregunta.pregunta,
            imatge: this.nuevaPregunta.imatge,
            // Cambiar la separación de comas a /
            respostes: this.nuevaPregunta.respostes.split('/').map(res => res.trim()),
            resposta_correcta: this.nuevaPregunta.resposta_correcta
        };

        fetch('/api/agregar-pregunta', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(nuevaPregunta)
        })
        .then(response => {
            if (response.ok) {
                alert('Pregunta añadida correctamente');
                this.nuevaPregunta = { pregunta: '', imatge: '', respostes: '', resposta_correcta: null };
                this.cargarPreguntas(); // Recargar preguntas después de añadir
            } else {
                alert('Error al añadir la pregunta');
            }
        })
        .catch(error => {
            console.error('Error al agregar la pregunta:', error);
            alert('Ocurrió un error al enviar la pregunta');
        });
    } else {
        alert('Por favor, completa todos los campos.');
    }
},


// Ruta para eliminar una pregunta por ID
app.delete('/api/eliminar-pregunta/:id', (req, res) => {
    const preguntaId = parseInt(req.params.id);

    // Leer el archivo JSON existente
    fs.readFile('test.json', 'utf8', (err, data) => {
        if (err) {
            return res.status(500).json({ message: 'Error al leer el archivo JSON.' });
        }

        let jsonData;
        try {
            jsonData = JSON.parse(data);
        } catch (parseError) {
            return res.status(500).json({ message: 'Error al analizar el archivo JSON.' });
        }

        // Filtrar la pregunta a eliminar
        jsonData.preguntes = jsonData.preguntes.filter(p => p.id !== preguntaId);

        // Guardar los cambios en el archivo JSON
        fs.writeFile('test.json', JSON.stringify(jsonData, null, 2), (writeErr) => {
            if (writeErr) {
                return res.status(500).json({ message: 'Error al escribir en el archivo JSON.' });
            }
            res.json({ message: 'Pregunta eliminada correctamente.' });
        });
    });
});

// Iniciar el servidor
app.listen(port, () => {
    console.log(`Servidor escuchando en http://localhost:${port}`);
});

