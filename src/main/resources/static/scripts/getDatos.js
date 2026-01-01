const baseURL = '';

export default function getDatos(endpoint) {
    return fetch(`${baseURL}${endpoint}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la red');
            }
            return response.json();
        })
        .catch(error => {
            console.error('Error al acceder al endpoint:', endpoint, error);
        });
}
