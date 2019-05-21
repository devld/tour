import { deleteSpot } from './spot'
import { deleteNotes } from './travel-notes'
import { toggleUserEnabled, deleteUserAccount } from './user'

export default {
  spot: {
    deleteSpot
  },
  travelNotes: {
    deleteNotes
  },
  user: {
    toggleUserEnabled,
    deleteUserAccount
  }
}

