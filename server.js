const express = require('express');
const fs = require('fs');
const bodyParser = require('body-parser');
const app = express();
const port = 3000;

// Middleware para analizar JSON
app.use(bodyParser.json());

// Servir archivos estáticos (para que Vue.js pueda cargar el frontend)
app.use(express.static(__dirname + '/public'));

// Ruta para agregar una nueva pregunta
app.post('/api/agregar-pregunta', (req, res) => {
    const nuevaPregunta = req.body;

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

        // Agregar la nueva pregunta al array
        jsonData.preguntes.push({
            id: jsonData.preguntes.length + 1,
            ...nuevaPregunta
        });

        // Guardar los cambios en el archivo JSON
        fs.writeFile('test.json', JSON.stringify(jsonData, null, 2), (writeErr) => {
            if (writeErr) {
                return res.status(500).json({ message: 'Error al escribir en el archivo JSON.' });
            }
            res.json({ message: 'Pregunta añadida correctamente.' });
        });
    });
});

// Iniciar el servidor
app.listen(port, () => {
    console.log(`Servidor escuchando en http://localhost:${port}`);
});
