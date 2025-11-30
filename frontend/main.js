import {createCalendar, destroyCalendar, TimeGrid} from '@event-calendar/core';
// Import CSS if your build tool supports it
import '@event-calendar/core/index.css';
import { getDeviceId, getProvidersLoggedIn } from './common';

const providerLoginElem = document.querySelector(".no-provider-login")
const mainContentElem = document.querySelector(".main-content")
const deviceId = getDeviceId()
const providers = await getProvidersLoggedIn(deviceId)
if (providers.length == 0) {
    providerLoginElem.style.display = "block"
} else {
    mainContentElem.style.display = "block"

    let ec = createCalendar(
        // HTML element the calendar will be mounted to
        document.getElementById('ec'),
        // Array of plugins
        [TimeGrid],
        // Options object
        {
            view: 'timeGridWeek',
            events: [
                // your list of events
            ]
        }
    );
    // If you later need to destroy the calendar then use
    // destroyCalendar(ec);
}

