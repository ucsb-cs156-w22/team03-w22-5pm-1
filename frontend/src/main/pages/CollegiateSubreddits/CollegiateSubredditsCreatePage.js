import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import CollegiateSubredditsForm from "main/components/CollegiateSubreddits/CollegiateSubredditsForm";
import { Navigate } from 'react-router-dom'
import { useBackendMutation } from "main/utils/useBackend";
import { toast } from "react-toastify";


export default function CollegiateSubredditsCreatePage() {

  const objectToAxiosParams = (collegiateSubreddit) => ({
    url: "/api/collegiateSubreddits/post",
    method: "POST",
    params: {
      name: collegiateSubreddit.name,
      location: collegiateSubreddit.location,
      subreddit: collegiateSubreddit.subreddit
    }
  });

  const onSuccess = (collegiateSubreddit) => {
    toast(`New CollegiateSubreddit Created - id: ${collegiateSubreddit.id} name: ${collegiateSubreddit.name}`);
  }

  const mutation = useBackendMutation(
    objectToAxiosParams,
     { onSuccess }, 
     // Stryker disable next-line all : hard to set up test for caching
     ["/api/collegiateSubreddits/all"]
     );

  const { isSuccess } = mutation

  const onSubmit = async (data) => {
    mutation.mutate(data);
  }

  if (isSuccess) {
    return <Navigate to="/collegiatesubreddits/list" />
  }

  return (
    <BasicLayout>
      <div className="pt-2">
        <h1>Create New collegiateSubreddit</h1>

        <CollegiateSubredditsForm submitAction={onSubmit} />

      </div>
    </BasicLayout>
  )
}