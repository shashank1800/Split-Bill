
import './App.css'
import { Routes, Route } from 'react-router-dom'
import AppToolBar from "./ui/common/AppToolBar";
import HomeScreenViewPager from './ui/main_ui/HomeScreenViewPager'
import SplashScreenFragment from './ui/splash_screen/SplashScreenFragment'
import NoMatch from './ui/common/NoMatch';

function App() {
  return (
    <>
      <AppToolBar />

      <Routes>
        <Route path='/home' element={<HomeScreenViewPager />} />
        <Route path='/' element={<SplashScreenFragment />} />
        <Route path='*' element={<NoMatch />} />
      </Routes>

    </>
  );
}

export default App
