import React from 'react'
import {render} from 'react-dom'
import { BrowserRouter } from 'react-router-dom'

import "normalize.css";
import "@blueprintjs/icons/lib/css/blueprint-icons.css";
import "@blueprintjs/core/lib/css/blueprint.css";
import "@blueprintjs/datetime/lib/css/blueprint-datetime.css";

import HomeRoutes from './HomeRoutes.js'

render((
  <BrowserRouter>
    <HomeRoutes/>
  </BrowserRouter>
), document.getElementById('root'));
