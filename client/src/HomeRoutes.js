import React from 'react'
import {render} from 'react-dom'
import { Switch, Route } from 'react-router-dom'

import HomePage from './HomePage.js'
import JournalPage from './JournalPage.js'
import AboutPage from './AboutPage.js'
import SignupPage from './SignupPage.js'

class HomeRoutes extends React.Component {

  contentRoutes () {
    return (
      <Switch>
        <Route exact path='/' component={HomePage}/>
        <Route path='/about' component={AboutPage}/>
        <Route path='/journal/:userId' component={JournalPage}/>
        <Route path='/signup' component={SignupPage} />
      </Switch>
    )
  }

  render () {
    return (
      <main>
        {this.contentRoutes()}
      </main>
    )
  }
}

export default HomeRoutes
