export function getDeviceId() {
    return localStorage.getItem("deviceId")
}

export function genDeviceId() {
    const deviceId = crypto.randomUUID()
    localStorage.setItem("deviceId", deviceId)
    return deviceId
}
