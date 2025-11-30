import { genDeviceId, getDeviceId, isProviderLoggedIn } from "./common";
const clientId = import.meta.env.VITE_GOOGLE_CALENDAR_API_CLIENT_ID;
const redirectURI = import.meta.env.VITE_GOOGLE_CALENDAR_API_REDIRECT_URI;

checkFailedLogin()
const deviceId = getDeviceId();
checkProviderStatus(deviceId)

async function checkProviderStatus(deviceId) {
    let providerElem = document.querySelector(".gmail-login-status")
    const loggedIn = await isProviderLoggedIn("google", deviceId)
    if (loggedIn) {
        providerElem.innerHTML = `<button class="gmail-logout">Log Out</button>`
    } else {
        providerElem.innerHTML = `<button class="gmail-login">Login</button>`
    }
    document.querySelector('.gmail-login')?.addEventListener("click", () => {
        gmailLogin();
    })
}

function gmailLogin() {
    const scope = 'https://www.googleapis.com/auth/calendar.readonly';
    const responseType = 'code';
    const accessType = 'offline';
    const prompt = 'consent';

    const stateData = {
        device_id: genDeviceId(),
        user_id: 'testuser123',
        provider: 'google'
    }

    const oauthUrl = `https://accounts.google.com/o/oauth2/v2/auth?client_id=${clientId}` +
                     `&redirect_uri=${redirectURI}` +
                     `&response_type=${responseType}` +
                     `&scope=${encodeURIComponent(scope)}` +
                     `&access_type=${accessType}` +
                     `&prompt=${prompt}` +
                     `&state=${encodeURIComponent(JSON.stringify(stateData))}`;

    window.location.href = oauthUrl;
}

function checkFailedLogin() {
    const params = new URLSearchParams(window.location.search)
    if (!params.has('login_status')) return

    const modal = document.querySelector('.global-modal')
    const status = params.get('login_status')
    if (modal && status == 'failed') {
        const body = modal.querySelector('.global-modal-body')
        body.textContent = "Failed to log in. Please try again later."
        modal.style.display = 'block';
    }
}

