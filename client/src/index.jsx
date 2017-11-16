import React from 'react'
import {render} from 'react-dom'
import { BrowserRouter } from 'react-router-dom'

import HomeRoutesPre from './HomeRoutesPre.js'

render((
  <BrowserRouter>
    <HomeRoutesPre/>
  </BrowserRouter>
), document.getElementById('root'));
