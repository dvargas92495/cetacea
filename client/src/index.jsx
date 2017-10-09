import React from 'react';
import {render} from 'react-dom';

import NewComponent from './NewComponent.js'

class App extends React.Component {
  render () {
    return (
      <div>
        <p> Hello binch </p>
        <NewComponent />
      </div>
    )
  }
}

render(<App/>, document.getElementById('app'));
