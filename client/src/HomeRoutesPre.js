import React from 'react'
import {render} from 'react-dom'
import { Switch, Route } from 'react-router-dom'

import HomePagePre from './HomePagePre.js'
import JournalPage from './JournalPage.js'
import AboutPage from './AboutPage.js'

class HomeRoutes extends React.Component {

  contentRoutes () {
    return (
      <Switch>
        <Route exact path='/' component={HomePage}/>
        <Route path='/about' component={AboutPage}/>
        <Route path=''
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
