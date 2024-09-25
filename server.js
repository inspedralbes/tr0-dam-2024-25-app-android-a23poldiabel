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

// Ruta para agregar una nueva pregunta
app.post('/api/agregar-pregunta', (req, res) => {
    const nuevaPregunta = req.body;

    // Validar la nueva pregunta
    if (!nuevaPregunta.pregunta || !nuevaPregunta.imatge || !nuevaPregunta.respostes || nuevaPregunta.resposta_correcta === null) {
        return res.status(400).json({ message: 'Por favor, completa todos los campos.' });
    }

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

        // Asignar un nuevo ID a la pregunta
        const nuevoId = jsonData.preguntes.length > 0 ? Math.max(jsonData.preguntes.map(p => p.id)) + 1 : 1;

        // Crear la nueva pregunta
        const pregunta = {
            id: nuevoId,
            pregunta: nuevaPregunta.pregunta,
            imatge: nuevaPregunta.imatge,
            respostes: nuevaPregunta.respostes.split('/').map(res => res.trim()),
            resposta_correcta: nuevaPregunta.resposta_correcta
        };

        // Agregar la nueva pregunta al JSON
        jsonData.preguntes.push(pregunta);

        // Guardar los cambios en el archivo JSON
        fs.writeFile('test.json', JSON.stringify(jsonData, null, 2), (writeErr) => {
            if (writeErr) {
                return res.status(500).json({ message: 'Error al escribir en el archivo JSON.' });
            }
            res.status(201).json({ message: 'Pregunta añadida correctamente.', pregunta });
        });
    });
});

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
