const clientId = import.meta.env.VITE_GOOGLE_CALENDAR_API_CLIENT_ID;
const redirectURI = import.meta.env.VITE_GOOGLE_CALENDAR_API_REDIRECT_URI;

document.querySelector('.gmail-login').onclick = () => {
    gmailLogin()
}

function gmailLogin() {
    const scope = 'https://www.googleapis.com/auth/calendar.readonly';
    const responseType = 'code';
    const accessType = 'offline';
    const prompt = 'consent';

    const stateData = {
        device_id: genDeviceId(),
        user_id: 'testuser123'
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

function genDeviceId() {
    deviceId = crypto.randomUUID();
    localStorage.setItem("deviceId", deviceId);
    return deviceId
}