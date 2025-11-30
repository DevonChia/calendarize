import { genDeviceId, getDeviceId } from "./common";
const clientId = import.meta.env.VITE_GOOGLE_CALENDAR_API_CLIENT_ID;
const redirectURI = import.meta.env.VITE_GOOGLE_CALENDAR_API_REDIRECT_URI;

checkFailedLogin()
const deviceId = getDeviceId();
if (deviceId) {
    isProviderLoggedIn("google", deviceId)
}

document.querySelector('.gmail-login').onclick = () => {
    gmailLogin()
}

function isProviderLoggedIn(provider, deviceId){
    if (deviceId === null) return false

    console.log(deviceId)
    const userId = "testuser123";

    fetch('api/verify-login',  {
        method : 'POST',
        headers : {
            'Content-type' : 'application/json'
        },
        body : JSON.stringify({
            'user-id' : userId,
            'device-id' : deviceId,
            'provider' : provider
        })
    })
        .then(response => response.json())
        .then(data => console.log(data))
        .catch(error => console.error(error))




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

