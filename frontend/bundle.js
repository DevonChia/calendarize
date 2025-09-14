import {createCalendar, destroyCalendar, TimeGrid} from '@event-calendar/core';
// Import CSS if your build tool supports it
import '@event-calendar/core/index.css';

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