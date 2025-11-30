export function getDeviceId() {
    return localStorage.getItem("deviceId")
}

export function genDeviceId() {
    const deviceId = crypto.randomUUID()
    localStorage.setItem("deviceId", deviceId)
    return deviceId
}

export async function getProvidersLoggedIn(deviceId) {
    const providers = ["google", "microsoft"]

    const all = await Promise.all(
        providers.map(async provider => {
            const loggedIn = await isProviderLoggedIn(provider, deviceId)
            return loggedIn ? provider : null
        })
    )
    const res = all.filter(p => p !== null)
    return res
}

export async function isProviderLoggedIn(provider, deviceId){
    if (deviceId === null) return false
    const userId = "testuser123";
    try {
        const res = await fetch('api/verify-login',  {
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
        const data = await res.json();
        return data["logged_in"]
    } catch (err) {
        console.error(err)
        return false
    }
}