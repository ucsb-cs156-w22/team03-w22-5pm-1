import { render, waitFor, fireEvent } from "@testing-library/react";
import CollegiateSubredditsForm from "main/components/CollegiateSubreddits/CollegiateSubredditsForm";
import { collegiateSubredditsFixtures } from "fixtures/collegiateSubredditsFixtures";
import { BrowserRouter as Router } from "react-router-dom";

const mockedNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockedNavigate
}));


describe("CollegiateSubredditsForm tests", () => {

    test("renders correctly ", async () => {

        const { getByText } = render(
            <Router  >
                <CollegiateSubredditsForm />
            </Router>
        );
        await waitFor(() => expect(getByText(/Name/)).toBeInTheDocument());
        await waitFor(() => expect(getByText(/Create/)).toBeInTheDocument());
    });


    test("renders correctly when passing in a CollegiateSubreddit ", async () => {

        const { getByText, getByTestId } = render(
            <Router  >
                <CollegiateSubredditsForm initialCollegiateSubreddits={collegiateSubredditsFixtures.oneSubreddit} />
            </Router>
        );
        await waitFor(() => expect(getByTestId(/CollegiateSubredditsForm-id/)).toBeInTheDocument());
        expect(getByText(/Id/)).toBeInTheDocument();
        expect(getByTestId(/CollegiateSubredditsForm-id/)).toHaveValue("1");
    });

    test("Correct Error messsages on missing input", async () => {

        const { getByTestId, getByText } = render(
            <Router  >
                <CollegiateSubredditsForm />
            </Router>
        );
        await waitFor(() => expect(getByTestId("CollegiateSubredditsForm-submit")).toBeInTheDocument());
        const submitButton = getByTestId("CollegiateSubredditsForm-submit");

        fireEvent.click(submitButton);

        await waitFor(() => expect(getByText(/Name is required./)).toBeInTheDocument());
        expect(getByText(/Location is required./)).toBeInTheDocument();
        expect(getByText(/Subreddit is required./)).toBeInTheDocument();

    });

    test("No Error messsages on good input", async () => {

        const mockSubmitAction = jest.fn();


        const { getByTestId, queryByText } = render(
            <Router  >
                <CollegiateSubredditsForm submitAction={mockSubmitAction} />
            </Router>
        );
        await waitFor(() => expect(getByTestId("CollegiateSubredditsForm-name")).toBeInTheDocument());

        const nameField = getByTestId("CollegiateSubredditsForm-name");
        const locationField = getByTestId("CollegiateSubredditsForm-location");
        const subredditField = getByTestId("CollegiateSubredditsForm-subreddit");
        const submitButton = getByTestId("CollegiateSubredditsForm-submit");

        fireEvent.change(nameField, { target: { value: 'UCSB' } });
        fireEvent.change(locationField, { target: { value: 'Santa Barbara' } });
        fireEvent.change(subredditField, { target: { value: 'ucsb' } });
        fireEvent.click(submitButton);

        await waitFor(() => expect(mockSubmitAction).toHaveBeenCalled());

        expect(queryByText(/Name is required./)).not.toBeInTheDocument();
        expect(queryByText(/Location is required./)).not.toBeInTheDocument();
        expect(queryByText(/Subreddit is required./)).not.toBeInTheDocument();

    });


    test("Test that navigate(-1) is called when Cancel is clicked", async () => {

        const { getByTestId } = render(
            <Router  >
                <CollegiateSubredditsForm />
            </Router>
        );
        await waitFor(() => expect(getByTestId("CollegiateSubredditsForm-cancel")).toBeInTheDocument());
        const cancelButton = getByTestId("CollegiateSubredditsForm-cancel");

        fireEvent.click(cancelButton);

        await waitFor(() => expect(mockedNavigate).toHaveBeenCalledWith(-1));

    });

});


