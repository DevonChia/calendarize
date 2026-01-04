import { renderHome } from "./pages/home";
import { renderDashboard } from "./pages/user-dashboard";
import { renderNotFound } from "./pages/404";

function router() {
    console.log("at main.js, path :: " + window.location.pathname)
    const path = window.location.pathname

    switch (path) {
        case '/':
            renderHome()
            break
        case '/dashboard':
            renderDashboard()
            break
        default:
            renderNotFound()
            break
    }
}

router()

window.addEventListener('popstate', router);

export function routeTo(path) {
    window.history.pushState({}, '', path)
    router()
}