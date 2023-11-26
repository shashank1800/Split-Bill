import React, { useEffect, useState } from 'react';
import GroupsEntity from '../../data/GroupEntity';
import * as groupRepository from './GroupRepository';
import axios from 'axios';
import * as APIConstants from '../utils/APIConstants';
import { Avatar, Badge, BottomNavigation, BottomNavigationAction, Box, Fab, Paper, Stack, Typography } from '@mui/material';
import GroupOutlinedIcon from '@mui/icons-material/GroupOutlined';
import WifiTetheringIcon from '@mui/icons-material/WifiTethering';
import FoundationIcon from '@mui/icons-material/Foundation';
import AddIcon from '@mui/icons-material/Add';
import './MainUi.css';
import { deepOrange, deepPurple, red } from '@mui/material/colors';


function HomeScreenViewPager() {

    const [users, setUsers] = useState([]);
    const [groupName, setGroupName] = useState('');
    const [showAddDialog, setShowAddDialog] = useState(false);
    const token = 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmNkZWZnaCIsImV4cCI6MTY4NDE2ODQ1NywiaWF0IjoxNjg0MTUwNDU3fQ.fHRKJdzBKt6iy8R1GoIE4xbap68WOWFScuJ4SdBW5smBBgQV6BXkZCmrEwsNJKMbmLJ3KlCMMPneJewuVdHDtg';

    let group = null;

    // useEffect(() => {

    //     if (!showAddDialog) {
    //         ; (async () => {
    //             const response = await groupRepository.getAllGroups();
    //             setUsers(response.data);
    //         })();
    //     }
    // }, [showAddDialog]);

    const handleChange = (event) => {
        setGroupName(event.target.value);
    };

    const addUser = (event) => {
        // event.preventDefault();

        // group = GroupsEntity.create(null, groupName, null, 49);

        // ; (async () => {
        //     await groupRepository.insert(group.value);
        //     setShowAddDialog(false);
        // })();

    };

    const showAddUserDialog = function () {
        setShowAddDialog(true);
    }

    return (
        <>

            <Stack direction="row" sx={{ display: 'flex' }} >

                <Paper direction="row" sx={{ flexGrow: 1, m: 2, p: 1, bgcolor: '#fff' }} >
                    <Badge
                        overlap="circular"
                        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
                        badgeContent={
                            <Avatar sx={{width: 22, height: 22, border: `2px solid black`}} />
                        }
                    >
                        <Avatar sx={{width: 100, height: 100}}  />
                    </Badge>
                    <Typography variant="h6">Shashank Bhat</Typography>
                </Paper>

                {/* Middle box */}
                <Paper elevation={4} sx={{ flexGrow: 2, m: 2, p: 1, bgcolor: '#fff' }} >

                    <Box sx={{ display: 'flex' }}>

                        <Stack direction="column" sx={{ flexGrow: 1 }} >


                            <Typography variant="h4" sx={{ my: 2 }} gutterBottom>
                                Evening Snacks
                            </Typography>



                            <Box sx={{ display: 'flex' }}>

                                <Stack direction="row" sx={{ flexGrow: 1 }} >
                                    <Avatar>HS</Avatar>
                                    <Avatar sx={{ bgcolor: deepOrange[500] }}>BN</Avatar>
                                    <Avatar sx={{ bgcolor: deepPurple[500] }}>OP</Avatar>
                                </Stack>
                                <Typography variant="h6" sx={{ my: 2 }} gutterBottom>
                                    0 minutes ago, 11:25 PM
                                </Typography>
                            </Box>



                        </Stack>

                        <GroupOutlinedIcon sx={{ color: '#05cf8f', fontSize: 50 }}>TEST</GroupOutlinedIcon>
                    </Box>
                </Paper>

                <Paper elevation={4} sx={{ flexGrow: 1, m: 2, p: 1, bgcolor: '#fff' }}>

                </Paper>

            </Stack>


            {/* <div>

                <ul>
                    {users.map((group, index) => (
                        <li key={index}>{group.group.name}</li>
                    ))}
                </ul>
                <div>
                    <button onClick={showAddUserDialog}>Add Group</button>
                </div>
            </div>

            {
                showAddDialog &&
                <div>
                    <form onSubmit={addUser}>
                        <input name="name" placeholder="Group Name"
                            value={groupName} onChange={handleChange} />
                        <button type="submit" >Add</button>
                    </form>
                </div>
            } */}

            {/* <FloatingButton /> */}
            {/* <BottomBar /> */}
        </>
    )
}

const FloatingButton = () => {
    return (
        <Fab color="primary" aria-label="add" className='floating-action-button'>
            <AddIcon />
        </Fab>
    );
};

const BottomBar = () => {
    const [value, setValue] = useState(0);
    return (
        <BottomNavigation
            showLabels
            value={value}
            onChange={(event, newValue) => {
                setValue(newValue);
            }}
        >
            <BottomNavigationAction label="Groups" icon={<FoundationIcon />} />
            <BottomNavigationAction label="People" icon={<WifiTetheringIcon />} />
            <BottomNavigationAction label="Profile" icon={<FoundationIcon />} />
        </BottomNavigation>
    );
};

export default HomeScreenViewPager;