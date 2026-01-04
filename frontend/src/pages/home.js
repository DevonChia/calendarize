import {createCalendar, destroyCalendar, TimeGrid} from '@event-calendar/core';
// Import CSS if your build tool supports it
import '@event-calendar/core/index.css';
import { getDeviceId, getProvidersLoggedIn } from '../utils/common';
import { routeTo } from '../main';

export async function renderHome() {
    document.getElementById("app").innerHTML = `
        <div class="header-bar">
            <h1>HELLO HI!!</h1>
            <div class="nav-dashboard">
                <a href="/dashboard" id="route-dashboard">Dashboard</a>
            </div>
            
        </div>
        <div class="main-content" style="display: none;">
            <button class="tester-btn">Click tester</button>
            <div class="ec-container">
                <div id="ec"></div>
            </div>
        </div>
    `

    document.getElementById('route-dashboard').addEventListener('click', e => {
        e.preventDefault()
        routeTo('/dashboard')
    })

    document.querySelector('.tester-btn').addEventListener('click', async ()=>{
        
        console.log('loading init data..')
        data = await loadData()
        console.log(data);
        document.querySelector('.tester').innerHTML= data.vals;

    })

    const userId = "testuser123";
    
    const mainContentElem = document.querySelector(".main-content")
    const dashboardNav = document.querySelector("#route-dashboard")
    const deviceId = getDeviceId()
    const providers = await getProvidersLoggedIn(deviceId)
    if (providers.length == 0) {
        dashboardNav.textContent = "Log in to your email providers to get started!"
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
                ],
                // datesSet: function(info) {
                //     // console.log('View Start Date:', info.start); // JavaScript Date object
                //     // console.log('View End Date (exclusive):', info.end); // JavaScript Date object
                //     // console.log('View Start Date (string):', info.startStr); // ISO8601 string
                //     // console.log('View End Date (string/exclusive):', info.endStr); // ISO8601 string
          
                // }
            }
        );
        // If you later need to destroy the calendar then use
        // destroyCalendar(ec);
    
        providers.map(async provider => {
            const calendarData = await getCalendarData(provider)
            populateCalendar(calendarData)
        })
    
        // const today = ec.getOption('date')
        // console.log("today ec :: " + today)
        // let view = ec.getView();
        // const firstDay = view.currentStart
        // console.log(view.currentStart)
        // const lastDay = view.currentEnd
        // console.log(view.currentEnd)
    
        const tmr = new Date()
        tmr.setDate(tmr.getDate() + 1)
        ec.setOption('date', tmr)
        const today = ec.getOption('date') // TODO: date updated, to make it show on UI
    
    
        
    }
}

async function loadData() {
    try {
        const response = await fetch('/getdata')
        console.log(response)

        const dataJson = await response.json()
        console.log(dataJson)

        return dataJson;
    } catch (error) {
        console.log("caught error222 :: " + error.message)
    }
}

async function getCalendarData(provider)
{
    const path = provider + "/" + userId + "/" + deviceId;
    const response = await fetch('api/calendar-data/' + path, {
        method: 'GET',
        headers: {
            'Content-type' : 'application/json'
        }
    })
    const data = await response.json();

    for (const d of data) {
        console.log(d['summary']);
        console.log(d['startDate']);

    }
}