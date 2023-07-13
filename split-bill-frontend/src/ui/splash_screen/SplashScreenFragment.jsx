import React, { useEffect } from 'react';
import appLogo from '../../assets/ic_launcher-playstore.png';
import { useNavigate } from "react-router-dom";

function SplashScreenFragment() {

    const navigate = useNavigate();

    useEffect(() => {
        setTimeout(() => {
            navigate("/home", { replace: true });
        }, 2000);
    }, []);

    return (<>


        <div className='center-all'>
            <img src={appLogo} className="logo" alt="Vite logo" />
            <div> Split Bill</div>
        </div>


    </>);
}

export default SplashScreenFragment;