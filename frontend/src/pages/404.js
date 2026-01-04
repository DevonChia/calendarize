export function renderNotFound() {
    document.getElementById('app').innerHTML = `
    <h1>
        Error 404. Page not found.
    </h1>
    <h3>
        <a href="/">Return to Home Page</a>
    </h3>
    `
}