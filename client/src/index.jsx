import React from 'react'
import {render} from 'react-dom'
import { BrowserRouter } from 'react-router-dom'

import HomeRoutes from './HomeRoutes.js'

render((
  <BrowserRouter>
    <HomeRoutes/>
  </BrowserRouter>
), document.getElementById('root'));
