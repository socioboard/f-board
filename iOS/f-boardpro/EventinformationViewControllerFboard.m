

#import "EventinformationViewControllerFboard.h"
#import "SUCacheFboard.h"
#import "CommentsViewControllerFboard.h"
#import "SingletonClass.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import <FBSDKShareKit/FBSDKShareKit.h>

@interface EventinformationViewControllerFboard ()

@end

@implementation EventinformationViewControllerFboard

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // self.view.backgroundColor = [UIColor colorWithRed:26.0f/255.0f green:160.0f/255.0f blue:214.0f/255.0f alpha:1.0f];
    self.view.backgroundColor=[UIColor whiteColor];
    
    self.eventadmistratorArray = [[NSMutableArray alloc]init];
    self.eventIdArray = [[NSMutableArray alloc]init];
    self.eventNameArray =[[NSMutableArray alloc]init];
    self.eventunreadArray =[[NSMutableArray alloc]init];
    
    //    self.navigationController.navigationBar.barTintColor = [UIColor colorWithRed:80.0f/255.0f green:105.0f/255.0f blue:183.0f/255.0f alpha:1.0f];
    //    [[UINavigationBar appearance] setTintColor:[UIColor blackColor]];
    
    UIView *view= [[UIView alloc]init];
    // view.backgroundColor = [UIColor colorWithRed:76.0f/255.0f green:104.0f/255.0f blue:187.0f/255.0f alpha:1.0f];
    
    view.backgroundColor=[UIColor whiteColor];
    view.frame = CGRectMake(0, 0, self.view.frame.size.width, 50);
    [self.view addSubview:view];
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [button addTarget:self
               action:@selector(aMethod)
     forControlEvents:UIControlEventTouchUpInside];
    [button setTitle:@"Back" forState:UIControlStateNormal];
    [button setTitleColor:[UIColor redColor] forState:
     UIControlStateNormal];
    button.frame = CGRectMake(0, 10.0, 160.0, 40.0);
    [self.view addSubview:button];
    
    [self createFollowTable];
    
    // Do any additional setup after loading the view.
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    [self fetchFeeds];
}
#pragma mark UI
-(void)createFollowTable
{
    UIView *headerView =[[UIView alloc]init];
    headerView.frame=CGRectMake(0, 0, SCREEN_WIDTH, 150);
    headerView.backgroundColor=[UIColor blackColor];
    
    UIView *footerView =[[UIView alloc]init];
    footerView.frame=CGRectMake(0, 0, SCREEN_WIDTH, 80);
    footerView.backgroundColor=[UIColor whiteColor];
    
   eventTableView=[[UITableView alloc]initWithFrame:CGRectMake(10, 50, self.view.frame.size.width-20,self.view.frame.size.height+10) style:UITableViewStylePlain];
    eventTableView.dataSource=self;
    eventTableView.delegate=self;
    eventTableView.backgroundColor=[UIColor whiteColor];
    eventTableView.tableHeaderView=headerView;
    eventTableView.tableFooterView=footerView;
    eventTableView.scrollsToTop=NO;
    [self.view addSubview:eventTableView];
    //---
    self.coverImageview =[[UIImageView alloc]init];
    self.coverImageview.frame=CGRectMake(0, 0, headerView.frame.size.width, headerView.frame.size.height);
    [self.coverImageview.layer setBorderColor: [[UIColor blackColor] CGColor]];
    [self.coverImageview.layer setBorderWidth:2];
    self.coverImageview.backgroundColor=[UIColor blackColor];
    [headerView addSubview:self.coverImageview];
    
    NSLog(@"image url %@",self.coverString);
    if(self.coverString)
    {
        
    }
    else
    {
        
    }
}
#pragma mark Fetch All Feeds
-(void)fetchFeeds
{
    BOOL connection= [[NSUserDefaults standardUserDefaults] boolForKey:@"ConnectionAvilable"];
    
    if (!connection) {
        UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No internet Connection" message:@"check your internet" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        return ;
    }
    NSIndexPath *path=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:path.row+path.section].token;
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
        NSString *str1 =self.eventidStr;
        
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"/%@/feed",str1] parameters:nil];
        
        
        NSMutableArray * findAllLikes=[[NSMutableArray alloc]init];
        NSMutableArray * findAllComments=[[NSMutableArray alloc]init];
        NSMutableArray * findAllShares = [[NSMutableArray alloc]init];
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error3)
         {
             NSString *eventId;
             for (NSDictionary *dic in [result objectForKey:@"data"] ) {
                 NSString *str1 = [dic objectForKey:@"picture"];
                 NSString *str2 = [dic objectForKey:@"message"];
                 NSLog(@"likes...%@",[dic objectForKey:@"likes"]);
                 if([dic objectForKey:@"likes"])
                 {
                     [findAllLikes addObject:[dic objectForKey:@"likes"]];
                 }
                 else
                 {
                     [findAllLikes addObject:[NSNumber numberWithInt:0]];
                 }
                 NSLog(@"comments...%@",[dic objectForKey:@"comments"]);
                 if([dic objectForKey:@"comments"])
                 {
                     [findAllComments addObject:[dic objectForKey:@"comments"]];
                 }
                 else
                 {
                     [findAllComments addObject:[NSNumber numberWithInt:0]];
                 }
                 NSLog(@"shares...%@",[dic objectForKey:@"shares"]);
                 if([[dic objectForKey:@"shares"] objectForKey:@"count"])
                 {
                     [findAllShares addObject:[[dic objectForKey:@"shares"] objectForKey:@"count"]];
                 }
                 else
                 {
                     [findAllShares addObject:[NSNumber numberWithInt:0]];
                 }
                 
                 
                 
                 eventId= [[dic objectForKey:@"from"]objectForKey:@"id"];
                 NSString *str4= [[dic objectForKey:@"from"]objectForKey:@"name"];
                 NSString *str5= [dic objectForKey:@"description"];
                 if (str1) {
                     [self.eventunreadArray addObject:str1];
                 }else{
                     [self.eventunreadArray addObject:@"nil"];
                 }
                 
                 
                 if (str2) {
                     [self.msgArray addObject:str2];
                 }else{
                     [self.msgArray addObject:@"nil"];
                 }
                 
                 if (eventId) {
                     [self.eventIdArray addObject:eventId];
                 }else{
                     [self.eventIdArray addObject:@"nil"];
                 }
                 
                 if (str4) {
                     [self.eventNameArray addObject:str4];
                 }else{
                     [self.eventNameArray addObject:@"nil"];
                 }
                 
                 if (str5) {
                     [self.descArray addObject:str5];
                 }else{
                     [self.descArray addObject:@"nil"];
                 }
                 
                 self.coverString = [result objectForKey:@"id"];
             }
             //   self.coverString = [[result objectForKey:@"cover"]objectForKey:@"source"];
             // self.coverString =[result objectForKey:@"id"];
             
             [self getBannertUrl:eventId];
             //Transferring Mutual Array in Global Array For Likes in all pages.
             allLikesInPage=[NSArray arrayWithArray:findAllLikes];
             allCommentInpage=[NSArray arrayWithArray:findAllComments];
             allSharesInpPage = [NSArray arrayWithArray:findAllShares];
            [eventTableView reloadData];
         }];
        
    }
}
-(void)getBannertUrl:(NSString *)eventId
{
    //Start an activity indicator here
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        
        //Call your function or whatever work that needs to be done
        //Code in this part is run on a background thread
       
        NSError * error=nil;
        NSURLResponse * urlResponse=nil;
        NSURL * url=[NSURL  URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@?fields=cover&access_token=%@",eventId,[SingletonClass sharedState].accessTokenString]];
        
        NSMutableURLRequest * getRequest=[[NSMutableURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:50];
        [getRequest setHTTPMethod:@"GET"];
        [getRequest addValue:@"application/x-www-form-urlencoded; charset=utf-8" forHTTPHeaderField:@"Content-Type"];
        NSData * data=[NSURLConnection sendSynchronousRequest:getRequest returningResponse:&urlResponse error:&error];
        if (data==nil)
        {
        }
        id response=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
        if(!error)
        {
            
        }
        
        NSLog(@" response of feeds %@",response);
        
        bannerImageUrl=[NSURL URLWithString:[[response objectForKey:@"cover"] objectForKey:@"source"]];
        
        dispatch_async(dispatch_get_main_queue(), ^(void) {
            
            //Stop your activity indicator or anything else with the GUI
            //Code here is run on the main thread
            [self.coverImageview setImageWithURL:bannerImageUrl placeholderImage:[UIImage imageNamed:@""]];
            
        });
    });
}
-(void)aMethod
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

-(NSString *)dateDiff:(NSString *)origDate {
    
    NSDateFormatter *df = [[NSDateFormatter alloc] init];
    [df setFormatterBehavior:NSDateFormatterBehavior10_4];
    [df setDateFormat:@"yyyy'-'MM'-'dd'T'HH':'mm':'ssZZZZ"];
    NSDate *convertedDate = [df dateFromString:origDate];
    NSDate *todayDate = [NSDate date];
    double ti = [convertedDate timeIntervalSinceDate:todayDate];
    ti = ti * -1;
    if(ti < 1) {
        return @"never";
    } else 	if (ti < 60) {
        return @"less than a minute ago";
    } else if (ti < 3600) {
        int diff = round(ti / 60);
        return [NSString stringWithFormat:@"%d minutes ago", diff];
    } else if (ti < 86400) {
        int diff = round(ti / 60 / 60);
        return[NSString stringWithFormat:@"%d hours ago", diff];
    } else if (ti < 2629743) {
        int diff = round(ti / 60 / 60 / 24);
        return[NSString stringWithFormat:@"%d days ago", diff];
    }  else if (ti > 31535999) {
        int diff = round(ti / 31535999);
        return[NSString stringWithFormat:@"%d Years ago", diff];
    }  else if (ti > 2628000) {
        int diff = round(ti / 2628000);
        return[NSString stringWithFormat:@"%d months ago", diff];
    }
    else {
        return @"never";
    }
}





//-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
//{
//}

// Start
/*
 -(void)commentButton:(UIBarButtonItem *)button{
 button.image=[[UIImage imageNamed:@"comment.png"]imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
 NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
 
 FBSDKAccessToken *token = [SUCache itemForSlot:index.section+index.row].token;
 
 if (token) {
 
 [FBSDKAccessToken setCurrentAccessToken:token];
 FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"/%@",[self.grpIdArray objectAtIndex:button.tag]] parameters:nil];
 
 [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error3) {
 
 
 PAPPhotoDetailsViewController *photoDetail=[[PAPPhotoDetailsViewController alloc] initWithDict:result];
 self.navigationController.navigationBarHidden=NO;
 [self.navigationController pushViewController:photoDetail animated:YES];
 
 
 }];
 
 }
 
 }
 
 -(void)likeButton:(UIBarButtonItem *)button{
 
 
 //button.image=[[UIImage imageNamed:@"like.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
 ////    button.image=[UIImage imageNamed:@"like.png"];
 NSArray *likeArr=[[[self.feedDictArray objectAtIndex:button.tag] objectForKey:@"likes"] objectForKey:@"data"];
 NSArray *commentArr=[[[self.feedDictArray objectAtIndex:button.tag] objectForKey:@"comments"] objectForKey:@"data"];
 NSString *idValue= [self.grpIdArray objectAtIndex:button.tag];
 NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
 //    NSLog(@"index path %d",index.section+index.row);
 FBSDKAccessToken *token = [SUCache itemForSlot:index.section+index.row].token;
 [FBSDKAccessToken setCurrentAccessToken:token];
 
 FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"%@/likes",idValue] parameters:nil HTTPMethod:@"POST"];
 
 [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id response, NSError *error) {
 NSString *str=[response valueForKey:@"success"];
 NSLog(@"%@",str);
 
 
 if ([response valueForKey:@"success"]) {
 
 button.title=[NSString stringWithFormat:@"likes %lu ",likeArr.count+1];
 }
 
 }];
 
 }
 
 
 /*-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
 {
 
 NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
 
 FBSDKAccessToken *token = [SUCache itemForSlot:index.section+index.row].token;
 
 if (token) {
 
 [FBSDKAccessToken setCurrentAccessToken:token];
 FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"/%@",[self.grpIdArray objectAtIndex:indexPath.section]] parameters:nil];
 
 [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error3) {
 
 if (self.photoDetail) {
 self.photoDetail=nil;
 }
 self.photoDetail=[[PAPPhotoDetailsViewController alloc] initWithDict:result];
 self.navigationController.navigationBarHidden=NO;
 [self.navigationController pushViewController:self.photoDetail animated:YES];
 
 }];
 
 }
 
 
 }
 
 
 
 -(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
 
 
 NSArray *likeArr=[[[self.feedDictArray objectAtIndex:section] objectForKey:@"likes"] objectForKey:@"data"];
 NSArray *commentArr=[[[self.feedDictArray objectAtIndex:section] objectForKey:@"comments"] objectForKey:@"data"];
 
 return 25;
 
 }
 
 
 
 
 
 //-(UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
 
 -(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
 
 UIView *view=[[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width,45)];
 view.backgroundColor=[UIColor blackColor];
 
 
 NSArray *likeArr=[[[self.feedDictArray objectAtIndex:section] objectForKey:@"likes"] objectForKey:@"data"];
 NSArray *commentArr=[[[self.feedDictArray objectAtIndex:section]  objectForKey:@"comments"] objectForKey:@"data"];
 NSString *likeTitle=[NSString stringWithFormat:@"Likes %lu",(unsigned long)likeArr.count];
 
 
 
 NSString *shareCount=[[[self.feedDictArray objectAtIndex:section] objectForKey:@"shares"] objectForKey:@"count"];
 NSString *commentTitle=[NSString stringWithFormat:@"Comments %lu",(unsigned long)commentArr.count];
 
 
 toolBar1 = [[UIToolbar alloc] initWithFrame:CGRectMake(0,0,tableView.frame.size.width , 30)];
 [toolBar1 setBarStyle:UIBarStyleDefault];
 toolBar1.backgroundColor=[UIColor redColor];
 //    toolBar1.tintColor=[UIColor blueColor];
 
 
 NSMutableArray *barItems = [[NSMutableArray alloc] init];
 
 UIBarButtonItem *likeButton = [[UIBarButtonItem alloc]initWithTitle:likeTitle style:UIBarButtonItemStyleBordered target:self action:@selector(likeButton:)];
 
 likeButton.tag=section;
 // [barItems addObject:likeButton];
 
 
 UIBarButtonItem *flexibleSpace1 = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:self action:@selector(commentButton:)];
 [flexibleSpace1 setTitle:@"Comments"];
 
 [barItems addObject:flexibleSpace1];
 
 UIBarButtonItem *commentButton = [[UIBarButtonItem alloc]initWithTitle:commentTitle style:UIBarButtonItemStyleBordered target:self action:@selector(commentButton:)];
 
 commentButton.tag=section;
 [barItems addObject:commentButton];
 
 UIBarButtonItem *flexibleSpace2 = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:self action:@selector(commentButton:)];
 [flexibleSpace2 setTitle:@"Comments"];
 
 
 [barItems addObject:flexibleSpace2];
 
 if (shareCount) {
 UIBarButtonItem *shareButton = [[UIBarButtonItem alloc]initWithTitle:[NSString stringWithFormat:@"Shares %@",shareCount]  style:UIBarButtonItemStyleBordered target:self action:@selector(commentButton:)];
 
 shareButton.tag=section;
 [barItems addObject:shareButton];
 }else{
 
 UIBarButtonItem *shareButton = [[UIBarButtonItem alloc]initWithTitle:@"Share"   style:UIBarButtonItemStyleBordered target:self action:@selector(commentButton:)];
 
 shareButton.tag=section;
 [barItems addObject:shareButton];
 
 
 }
 
 
 
 
 [toolBar1 setItems:barItems animated:YES];
 [view addSubview:toolBar1];
 
 return view;
 
 }
 
 */
// End
#pragma mark Table Delegates
-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    
    return 10;
    
}

-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    
    UIView *view1=[[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width,10)];
    view1.backgroundColor=[UIColor clearColor];
    
    
    return view1;
    
    
}





-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    
    return 25;
    
}


//-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{

-(UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    
    
    
    UIView *viewForFooter=[[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width,25)];
    viewForFooter.backgroundColor=[UIColor clearColor];
    
    
    
    for(int i=1;i<3;i++)
    {
        UIView *lineView=[[UIView alloc] initWithFrame:CGRectMake(100*i, 5, 0.5,25)];
        lineView.backgroundColor=[UIColor grayColor];
        [viewForFooter addSubview:lineView];
        
    }
    
    UIView *lineView1=[[UIView alloc] initWithFrame:CGRectMake(0, 5, SCREEN_WIDTH,0.5)];
    lineView1.backgroundColor=[UIColor grayColor];
    [viewForFooter addSubview:lineView1];
    
    UIView *lineView2=[[UIView alloc] initWithFrame:CGRectMake(0, 30, SCREEN_WIDTH,0.5)];
    lineView2.backgroundColor=[UIColor grayColor];
    [viewForFooter addSubview:lineView2];
    
    
    
    UIButton *likeButton =[[UIButton alloc]init];
    likeButton.frame=CGRectMake(0, 8, 50, 20);
    [likeButton setTitle:@"Like" forState:UIControlStateNormal];
    likeButton.titleLabel.font = [UIFont systemFontOfSize:12];
    [likeButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    
    [viewForFooter addSubview:likeButton];
    
    UILabel * likeDisplay=[[UILabel alloc]initWithFrame:CGRectMake(45, 8, 60, 20)];
    if([[allLikesInPage objectAtIndex:section] isKindOfClass:[NSDictionary class]] )
    {
        [likeButton setTitle:@"Likes" forState:UIControlStateNormal];
        likeDisplay.text=[NSString stringWithFormat:@"%lu",(unsigned long)[[[allLikesInPage objectAtIndex:section] objectForKey:@"data"] count]];
    }
    
    
    likeDisplay.font=[UIFont systemFontOfSize:12];
    [viewForFooter addSubview:likeDisplay];
    
    UIButton *commentButton =[[UIButton alloc]init];
    commentButton.frame=CGRectMake(90, 8, 100, 20);
    [commentButton setTitle:@"Comment" forState:UIControlStateNormal];
    commentButton.titleLabel.font = [UIFont systemFontOfSize:12];
    [commentButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    
    [viewForFooter addSubview:commentButton];
    
    
    UILabel * commentDisplay=[[UILabel alloc]initWithFrame:CGRectMake(175, 8, 60, 20)];
    if([[allCommentInpage objectAtIndex:section] isKindOfClass:[NSDictionary class]] )
    {
        
        [commentButton setTitle:@"Comments" forState:UIControlStateNormal];        commentDisplay.text=[NSString stringWithFormat:@"%lu",(unsigned long)[[[allCommentInpage objectAtIndex:section] objectForKey:@"data"] count]];
    }
    
    
    commentDisplay.font=[UIFont systemFontOfSize:12];
    [viewForFooter addSubview:commentDisplay];
    
    
    
    
    UIButton *shareButton =[[UIButton alloc]init];
    shareButton.frame=CGRectMake(180, 8, 100, 20);
    [shareButton setTitle:@"share" forState:UIControlStateNormal];
    shareButton.titleLabel.font = [UIFont systemFontOfSize:12];
    [shareButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    
    [viewForFooter addSubview:shareButton];
    
    UILabel * sharesDisplay=[[UILabel alloc]initWithFrame:CGRectMake(255, 8, 60, 20)];
    if(![[allSharesInpPage objectAtIndex:section] intValue]==0 )
    {
        
        [shareButton setTitle:@"shares" forState:UIControlStateNormal];       sharesDisplay.text=[NSString stringWithFormat:@"%@",[allSharesInpPage objectAtIndex:section]];
    }
    
    
    sharesDisplay.font=[UIFont systemFontOfSize:12];
    [viewForFooter addSubview:sharesDisplay];
    
    
    
    return viewForFooter;
    
    
}




-(NSInteger) numberOfSectionsInTableView:(UITableView *)tableView
{NSLog(@"Count image %ld",self.pictureArray.count);
    return self.eventIdArray.count;
}
-(NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSDictionary * tempDict=[self.allDataOfEventDetail objectAtIndex:indexPath.section];
    CGFloat heightOfText=[self calculateHeight:[tempDict objectForKey:@"message"]];
    //---------
    
    return MAX(80, heightOfText+80);
}

-(CGFloat)calculateHeight:(NSString *)stringData
{
    UILabel * lblDescription=[[UILabel alloc]init];
    lblDescription.font=[UIFont systemFontOfSize:12];
    // Create a paragraph style with the desired line break mode
    NSMutableParagraphStyle *paragraphStyle = [[NSMutableParagraphStyle alloc] init];
    paragraphStyle.lineBreakMode = NSLineBreakByWordWrapping;
    
    // Create the attributes dictionary with the font and paragraph style
    NSDictionary *attributes = @{
                                 NSFontAttributeName:lblDescription.font,
                                 NSParagraphStyleAttributeName:paragraphStyle
                                 };
    
    // Call boundingRectWithSize:options:attributes:context for the string
    CGRect textRect = [stringData boundingRectWithSize:CGSizeMake(self.view.frame.size.width-170,6000)
                                               options:NSStringDrawingUsesLineFragmentOrigin
                                            attributes:attributes
                                               context:nil];
    
    float height = textRect.size.height;
    // NSLog(@"height of row %f",height);
    return height;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Display1";
    
    TableCustomCellFboard *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil)
    {
        cell = [[TableCustomCellFboard alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    //cell.backgroundColor= [UIColor redColor];
//----------Retriving Data-------
    NSDictionary * tempDict=[self.allDataOfEventDetail objectAtIndex:indexPath.section];
    //-----------------
    cell.layer.cornerRadius=5;
    [cell.layer setBorderColor: [[UIColor blackColor] CGColor]];
    [cell.layer setBorderWidth:1];
    cell.timeLabel.text=@"";
    // NSString *timeStr=[self.dicArray objectAtIndex:indexPath.section];
    
    //  timeStr=[self dateDiff:timeStr];
    // cell.timeLabel.text=timeStr;
    
    
    
    
    NSString *str1 =[self.msgArray objectAtIndex:indexPath.section];
    NSString *str2 = [self.descArray objectAtIndex:indexPath.section];
    if (![str1 isEqualToString:@"nil"]) {
        cell.userNameDesc.text=str1;
    }else{
        if (str2) {
            cell.userNameDesc.text=str2;
        }else{
            cell.userNameDesc.text=@"Posted something via Facebook";
        }
    }
    CGFloat heightOfDescriptionLbl=[self calculateHeight:cell.userNameDesc.text];
    cell.userNameDesc.frame=CGRectMake(120, 40, self.view.frame.size.width-170,heightOfDescriptionLbl+10);
    cell.likeCountLabel.text = @"";
    cell.commentCountLabel.text=@"";
    NSString *picUrl =[self.pictureArray objectAtIndex:indexPath.section];
    
    
    if (![picUrl isEqualToString:@"nil"]) {
        [cell.userImage setImageWithURL:[NSURL URLWithString:picUrl] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }else{
        [cell.userImage setImageWithURL:[NSURL URLWithString:picUrl] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }
    
    NSString *fromIdImage = [self.eventIdArray objectAtIndex:indexPath.section];
    
    if (![fromIdImage isEqualToString:@"nil"]) {
        [cell.fromUserImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=small",fromIdImage]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }else{
        [cell.fromUserImage setImageWithURL:[NSURL URLWithString:picUrl] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }
    
    NSString *fromLabel1 = [self.eventNameArray objectAtIndex:indexPath.section ];
    cell.fromLabel.text = fromLabel1;
    CGFloat heightOfText=[self calculateHeight:[tempDict objectForKey:@"message"]];
    cell.userNameDesc.text=[tempDict objectForKey:@"message"] ;
    cell.userNameDesc.frame=CGRectMake(10, 60,cell.frame.size.width-40,heightOfText);
    //    NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc] initWithString:cell.fromLabel.text];
    //        // Add Background Color for Smooth rendering
    //    [attributedString setAttributes:@{NSBackgroundColorAttributeName:[UIColor blackColor]} range:NSMakeRange(0, attributedString.length)];
    //        // Add Main Font Color
    //    [attributedString setAttributes:@{NSForegroundColorAttributeName:[UIColor colorWithWhite:0.23 alpha:1.0]} range:NSMakeRange(0, attributedString.length)];
    //        // Add paragraph style
    //    NSMutableParagraphStyle *paragraphStyle = [[NSMutableParagraphStyle alloc] init];
    //    [paragraphStyle setLineBreakMode:NSLineBreakByWordWrapping];
    //    [attributedString setAttributes:@{NSParagraphStyleAttributeName:paragraphStyle} range:NSMakeRange(0, attributedString.length)];
    //        // Add Font
    //    [attributedString setAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:17]} range:NSMakeRange(0, attributedString.length)];
    //        // And finally set the text on the label to use this
    //        //    label.attributedText = attributedString;
    //
    //        // Phew. Now let's make the Bounding Rect
    //    CGRect expectedLabelSize= [attributedString boundingRectWithSize:CGSizeMake(cell.fromLabel.frame.size.width, MAXFLOAT) options:NSStringDrawingUsesLineFragmentOrigin context:nil];
    //
    //    cell.fromLabel.frame= expectedLabelSize;
    return cell;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

@end
