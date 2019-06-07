import React from 'react'
import {render} from 'react-dom'
import { Router } from 'react-router-dom'
import history from './history';

import HomeRoutes from './HomeRoutes.js'

render((
  <Router history={history}>
    <HomeRoutes/>
  </Router>
), document.getElementById('root'));
