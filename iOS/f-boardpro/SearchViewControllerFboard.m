//
//  SearchViewController.m
//  f-boardpro
//
//  Created by Sumit Ghosh on 14/09/15.
//  Copyright (c) 2015 socioboard. All rights reserved.
//

#import "SearchViewControllerFboard.h"
#import "TableCustomCellFboard.h"
#import "SUCacheFboard.h"
#import "SingletonClass.h"
#import "PaggeInformationViewControllerFboard.h"
#import "DisplayGroupViewControllerFboard.h"
#import "EventinformationViewControllerFboard.h"
#import "ProgressHUD.h"
@interface SearchViewControllerFboard()

@end

@implementation SearchViewControllerFboard

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    // For pages
    self.categoryArray = [[NSMutableArray alloc]init];
    self.nameArray = [[NSMutableArray alloc]init];
    self.idArray = [[NSMutableArray alloc]init];
    // For Groups
    self.admistratorArray = [[NSMutableArray alloc]init];
    self.unreadArray = [[NSMutableArray alloc]init];
    self.grpIdArray = [[NSMutableArray alloc]init];
    self.grpNameArray = [[NSMutableArray alloc]init];
    // For Events
    self.eventadmistratorArray = [[NSMutableArray alloc]init];
    self.eventunreadArray = [[NSMutableArray alloc]init];
    self.eventIdArray = [[NSMutableArray alloc]init];
    self.eventNameArray = [[NSMutableArray alloc]init];
    //------
    self.view.backgroundColor=[UIColor whiteColor];
    //---
    UIView *textfieldView = [[UIView alloc]init];
    textfieldView.frame = CGRectMake(0, 0, self.view.frame.size.width,70);
    textfieldView.backgroundColor =ThemeColorFboard;
    [self.view addSubview:textfieldView];
    //--
    searchTextField = [[UITextField alloc]init];
    searchTextField.frame= CGRectMake(5, 20, self.view.frame.size.width-10, 30);
    searchTextField.backgroundColor=[UIColor whiteColor];
    searchTextField.delegate=self;
    searchTextField.layer.cornerRadius=5;
    [textfieldView addSubview:searchTextField];
    //---
    UIImage *addsearchImage = [UIImage imageNamed:@"search_icon.png"];
    searchButton =[UIButton buttonWithType:UIButtonTypeRoundedRect];
    searchButton.frame=CGRectMake(textfieldView.frame.size.width-40, 22,25, 25 );
    [searchButton setBackgroundImage:addsearchImage forState:UIControlStateNormal];
    [ searchButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [ searchButton addTarget:self action:@selector(search) forControlEvents:UIControlEventTouchUpInside];
    [textfieldView addSubview: searchButton];
    [self searchUi];
    pageNo=0;
}


-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:YES];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(fetchUserLikedPages) name:@"CurrentUserChangedNotification" object:nil];
    self.navigationController.navigationBarHidden=YES;
    
    
}

-(void)viewDidDisappear:(BOOL)animated{
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"CurrentUserChangedNotification" object:nil];
}
#pragma mark Search UI and Methods
-(void)search
{
    [searchTextField resignFirstResponder];
    // serch scrollview
    [NSThread detachNewThreadSelector:@selector(addProgressHud) toTarget:self withObject:nil];
    switch (pageNo)
    {
        case 0:
        {
            [self fetchUserLikedPages];
        }
            break;
        case 1:
        {
            [self fetchGroupFeeds];
        }
            break;
        case 2:
        {
            [self fetchevents];
        }
            break;
        case 3:
        {
            [self fetchPeople];
        }
            break;
        case 4:
        {
            [self fetchPlaces];
 
        }
            break;
            
        default:
            break;
    }
}
-(void)searchUi
{
    
    UILabel * searchLblTitle;
    scrollViewPaging = [[UIScrollView alloc] init];
    scrollViewPaging.frame=CGRectMake(0, 70,SCREEN_WIDTH,SCREEN_HEIGHT);
    scrollViewPaging.bounces=NO;
    [self.view addSubview:scrollViewPaging];
    
    // a page is the width of the scroll view
    scrollViewPaging.pagingEnabled = YES;
    scrollViewPaging.showsHorizontalScrollIndicator = NO;
    scrollViewPaging.showsVerticalScrollIndicator = NO;
    scrollViewPaging.scrollsToTop = NO;
    scrollViewPaging.backgroundColor=[UIColor clearColor];
    scrollViewPaging.delegate =self;
   //-------
    UIView * viewForPageController=[[UIView alloc]init];
    viewForPageController.frame=CGRectMake(0,90,SCREEN_WIDTH,20);
    viewForPageController.backgroundColor=ThemeColorFboard;
    [self.view addSubview:viewForPageController];
//--------
    pageController = [[UIPageControl alloc] init];
    pageController.frame = CGRectMake(SCREEN_WIDTH/2-50,0, 100,20);
    pageController.backgroundColor=[UIColor clearColor];
    [viewForPageController addSubview:pageController];
    if(IS_IPHONE_6P)
    {
        pageController.frame = CGRectMake(SCREEN_WIDTH/2-50,0, 100,20);
        
    }
    else if (IS_IPHONE_6)
    {
        pageController.frame = CGRectMake(SCREEN_WIDTH/2-50,0, 100,20);
        
    }
    scrollViewPaging.contentSize = CGSizeMake(scrollViewPaging.frame.size.width * 5,scrollViewPaging.frame.size.height);
    pageController.currentPage = 0;
    pageController.numberOfPages =5;
        for (int i=0;i<=4; i++)
    {
        
        CGRect frame;
        frame.origin.x = scrollViewPaging.frame.size.width * i;
        frame.origin.y = 0;
        frame.size =scrollViewPaging.frame.size;
        
        switch (i) {
            case 0:
            {
                //   subView.backgroundColor=[UIColor redColor];
                searchLblTitle=[[UILabel alloc]init];
                searchLblTitle.frame=CGRectMake(0, 0, SCREEN_WIDTH, 20);
                searchLblTitle.text=@"Pages";
                searchLblTitle.textColor=[UIColor whiteColor];
                searchLblTitle.backgroundColor=ThemeColorFboard;
                searchLblTitle.textAlignment=NSTextAlignmentCenter;
                [scrollViewPaging addSubview:searchLblTitle];
                
            }
                break;
            case 1:
            {
                searchLblTitle=[[UILabel alloc]init];
                searchLblTitle.frame=CGRectMake(SCREEN_WIDTH,0, SCREEN_WIDTH, 20);
                searchLblTitle.backgroundColor=ThemeColorFboard;
                searchLblTitle.textColor=[UIColor whiteColor];
                searchLblTitle.textAlignment=NSTextAlignmentCenter;
                searchLblTitle.text=@"Groups";
                [scrollViewPaging addSubview:searchLblTitle];
                
            }
                //  subView.backgroundColor=[UIColor greenColor];
                break;
            case 2:
            {
                // subView.backgroundColor=[UIColor brownColor];
                searchLblTitle=[[UILabel alloc]init];
                searchLblTitle.frame=CGRectMake(2*SCREEN_WIDTH,0, SCREEN_WIDTH, 20);
                searchLblTitle.backgroundColor=ThemeColorFboard;
                searchLblTitle.textColor=[UIColor whiteColor];
                searchLblTitle.textAlignment=NSTextAlignmentCenter;
                searchLblTitle.text=@"Events";
                [scrollViewPaging addSubview:searchLblTitle];
                
            }
                break;
            case 3:
            {
                // subView.backgroundColor=[UIColor brownColor];
                searchLblTitle=[[UILabel alloc]init];
                searchLblTitle.frame=CGRectMake(3*SCREEN_WIDTH,0, SCREEN_WIDTH, 20);
                searchLblTitle.backgroundColor=ThemeColorFboard;
                searchLblTitle.textColor=[UIColor whiteColor];
                searchLblTitle.textAlignment=NSTextAlignmentCenter;
                searchLblTitle.text=@"Peoples";
                [scrollViewPaging addSubview:searchLblTitle];
                
            }
                break;
            case 4:
            {
                // subView.backgroundColor=[UIColor brownColor];
                searchLblTitle=[[UILabel alloc]init];
                searchLblTitle.frame=CGRectMake(4*SCREEN_WIDTH,0, SCREEN_WIDTH, 20);
                searchLblTitle.backgroundColor=ThemeColorFboard;
                searchLblTitle.textColor=[UIColor whiteColor];
                searchLblTitle.textAlignment=NSTextAlignmentCenter;
                searchLblTitle.text=@"Places";
                [scrollViewPaging addSubview:searchLblTitle];
                
            }
                break;
            default:
                break;
        }
        //add imageview
        [self addDifferentSearchTables:i];
    }
    
    //        UIView * lineBgView=[[UIView alloc]init];
    //        lineBgView.frame=CGRectMake(0, 20,3*SCREEN_WIDTH,8);
    //        lineBgView.backgroundColor=ThemeColor;
    //        [scrollViewPaging addSubview:lineBgView];
    //        titleLineView.backgroundColor=[UIColor whiteColor];
    //        titleLineView.frame=CGRectMake(SCREEN_WIDTH/2-100,0,200,5);
    //        [lineBgView addSubview:titleLineView];
    
    
}
-(void)addDifferentSearchTables:(int)pageNoLocal
{
    switch (pageNoLocal)
    {
        case 0:
        {
            pageTableView=[[UITableView alloc]init];
            pageTableView.frame=CGRectMake(0,40,SCREEN_WIDTH, SCREEN_HEIGHT-100);
            pageTableView.backgroundColor=[UIColor whiteColor];
            pageTableView.delegate=self;
            pageTableView.dataSource=self;
            [scrollViewPaging addSubview:pageTableView];
            pageTableView.tableFooterView=[self footerView];
            
        }
            break;
        case 1:
        {
            // TableView For Groups
            
            groupTableView=[[UITableView alloc]init];
            groupTableView.frame=CGRectMake(SCREEN_WIDTH,40,SCREEN_WIDTH,SCREEN_HEIGHT-100);
            groupTableView.backgroundColor=[UIColor whiteColor];
            groupTableView.delegate=self;
            groupTableView.dataSource=self;
            [scrollViewPaging addSubview:groupTableView];
            groupTableView.tableFooterView=[self footerView];

        }
            break;
        case 2:
        {
            // TableView For Events
            
            eventTableView=[[UITableView alloc]init];
            eventTableView.frame=CGRectMake(SCREEN_WIDTH*2,40,SCREEN_WIDTH,SCREEN_HEIGHT-100);
            eventTableView.backgroundColor=[UIColor whiteColor];
            eventTableView.delegate=self;
            eventTableView.dataSource=self;
            [scrollViewPaging addSubview:eventTableView];
            eventTableView.tableFooterView=[self footerView];

        }
            break;
        case 3:
        {
            // TableView For Peoples
            
            peopleTableView=[[UITableView alloc]init];
            peopleTableView.frame=CGRectMake(SCREEN_WIDTH*3,40,SCREEN_WIDTH,SCREEN_HEIGHT-100);
            peopleTableView.backgroundColor=[UIColor whiteColor];
            peopleTableView.delegate=self;
            peopleTableView.dataSource=self;
            [scrollViewPaging addSubview:peopleTableView];
            peopleTableView.tableFooterView=[self footerView];

        }
            break;
        case 4:
        {
            // Table view For Places
            
            
            placeTableView=[[UITableView alloc]init];
            placeTableView.frame=CGRectMake(SCREEN_WIDTH*4,40,SCREEN_WIDTH,SCREEN_HEIGHT-100);
            placeTableView.backgroundColor=[UIColor whiteColor];
            
            placeTableView.delegate=self;
            placeTableView.dataSource=self;
            [scrollViewPaging addSubview:placeTableView];
            placeTableView.tableFooterView=[self footerView];

        }
            break;
        default:
            break;
    }
}
-(UIView*)footerView
{
    UIView * footerViewTable=[[UIView alloc]init];
    footerViewTable.frame=CGRectMake(0, 0, SCREEN_WIDTH,60);
    return footerViewTable;
}

#pragma mark Scroll View Delegates
- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate
{
    NSLog(@"scroll view x %f",scrollView.frame.origin.x);
}
- (void)scrollViewDidEndScrollingAnimation:(UIScrollView *)scrollView
{
    
}
- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    if(scrollView==scrollViewPaging)
    {
        CGFloat viewWidth=scrollView.contentOffset.x;
        float pageNoLocal=viewWidth/SCREEN_WIDTH;
        if(pageController)
        {
            pageController.currentPage =pageNoLocal;
        }
        pageNo=pageNoLocal;
    }
}

# pragma mark TableView Delegates and Datasource

-(NSInteger) numberOfSectionsInTableView:(UITableView *)tableView
{
    
        return 1;
  
    
   
}
-(NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{    if(tableView==pageTableView)
     {
     return self.idArray.count;
     }
   if(tableView==groupTableView)
   {
       return groupData.count;
   }
    
    if(tableView==eventTableView)
    {
        return eventData.count;
    }
    else if(tableView==peopleTableView)
    {
        return peopleData.count;
    }
    else if(tableView==placeTableView)
    {
          return placeData.count;
    }
    else
        return 5;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(tableView==placeTableView)
    {
        NSDictionary * placeDataDict=[placeData objectAtIndex:indexPath.row];
        NSString * str1=[placeDataDict objectForKey:@"name"];
        CGFloat hhPlaceCell=[self calculateHeight:str1 width:SCREEN_WIDTH-160];
        return hhPlaceCell+95;
    }
    return 120;
}

-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 5;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Display";
    
    TableCustomCellFboard *cell = (TableCustomCellFboard*)[tableView cellForRowAtIndexPath:indexPath];
    
    if (cell == nil)
    {
        cell = [[TableCustomCellFboard alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    cell.backgroundColor=[UIColor clearColor];
    
    if(tableView==pageTableView)
    {
    
    NSString *picUrl =[self.idArray objectAtIndex:indexPath.row];
    
    
    if (![picUrl isEqualToString:@"nil"]) {
        [cell.userImage setImageWithURL:[NSURL URLWithString:[NSString
                                                              stringWithFormat:@"https://graph.facebook.com/%@/picture?type=large",picUrl]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
        //cell.userNameDesc.frame=CGRectMake(150, 5, 150, 100);
        
    }
    else
    {
        [cell.userImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=large",picUrl]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }
    
    NSString *str1 =[self.nameArray objectAtIndex:indexPath.row];
    
    if (![str1 isEqualToString:@"nil"]) {
        cell.userNameDesc.text=str1;
    }else{
        cell.userNameDesc.text=@"nothing";
    }
    
    NSString *str2 = [self.categoryArray objectAtIndex:indexPath.row];
    cell.fromLabel.text = str2;
            
            
            cell.likeCountLabel.text = @"";
            cell.commentCountLabel.text = @"";
        
        }
    
    if(tableView==groupTableView)
    {
        NSDictionary * groupDataDict=[groupData objectAtIndex:indexPath.row];
        
        NSString *fromIdImage = [groupDataDict objectForKey:@"id"];
        
        if (![fromIdImage isEqualToString:@"nil"]) {
            [cell.userImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=large",fromIdImage]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
        }else{
           [cell.userImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=large",fromIdImage]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
            
            
//            [cell.userImage setImageWithURL:[NSURL URLWithString:fromIdImage] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
        }
        
        
        NSString *str1 =[groupDataDict objectForKey:@"name"];
        
        if (![str1 isEqualToString:@"nil"]) {
            cell.userNameDesc.text=str1;
        }else{
            cell.userNameDesc.text=@"nothing";
        }
        
        
        NSString *str2 = [groupDataDict objectForKey:@"privacy"];
        cell.fromLabel.text = str2;
   
       // cell.likeCountLabel.text = @"";
       // cell.commentCountLabel.text = @"";
        

    }
    if(tableView==eventTableView)
    {
        NSDictionary * eventDataDict=[eventData objectAtIndex:indexPath.row];
        
        NSString *fromIdImage = [eventDataDict objectForKey:@"id"];
        
        if (![fromIdImage isEqualToString:@"nil"]) {
            [cell.userImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=large",fromIdImage]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
        }else{
            [cell.userImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=large",fromIdImage]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
            
            
            //            [cell.userImage setImageWithURL:[NSURL URLWithString:fromIdImage] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
        }
        
        
        NSString *str1 =[eventDataDict objectForKey:@"name"];
        
        if (![str1 isEqualToString:@"nil"]) {
            cell.userNameDesc.text=str1;
        }else{
            cell.userNameDesc.text=@"nothing";
        }
        
        
        NSString *str2 = [eventDataDict objectForKey:@"privacy"];
        cell.fromLabel.text = str2;
        
        // cell.likeCountLabel.text = @"";
        // cell.commentCountLabel.text = @"";
    }
    if(tableView==peopleTableView)
    {
        NSDictionary * eventDataDict=[peopleData objectAtIndex:indexPath.row];
        
        NSString *fromIdImage = [eventDataDict objectForKey:@"id"];
        
        if (![fromIdImage isEqualToString:@"nil"]) {
            [cell.userImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=large",fromIdImage]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
        }else{
            [cell.userImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=large",fromIdImage]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
            
            
            //            [cell.userImage setImageWithURL:[NSURL URLWithString:fromIdImage] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
        }
        
        
        NSString *str1 =[eventDataDict objectForKey:@"name"];
        
        if (![str1 isEqualToString:@"nil"]) {
            cell.userNameDesc.text=str1;
        }else{
            cell.userNameDesc.text=@"nothing";
        }
        
    
        // cell.likeCountLabel.text = @"";
        // cell.commentCountLabel.text = @"";
    }
    if(tableView==placeTableView)
    {
        CGFloat hhPlaceCell;
        static NSString *cellIdentifier=@"SearchingIdentifier";
        
        TableCustomCellFboard *cell = (TableCustomCellFboard*)[tableView cellForRowAtIndexPath:indexPath];
        NSDictionary * placeDataDict;
        if (cell == nil)
        {
            cell = [[TableCustomCellFboard alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
        }
        
        placeDataDict=[placeData objectAtIndex:indexPath.row];
        NSString *fromIdImage = [placeDataDict objectForKey:@"id"];
        
        if (![fromIdImage isEqualToString:@"nil"]) {
            [cell.userImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=large",fromIdImage]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
        }else
        {
            [cell.userImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=large",fromIdImage]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
            
            
            //            [cell.userImage setImageWithURL:[NSURL URLWithString:fromIdImage] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
        }
        
        NSString *str1 =[placeDataDict objectForKey:@"name"];
        
        hhPlaceCell=[self calculateHeight:str1 width:SCREEN_WIDTH-160];
        cell.containerView.frame=CGRectMake(10,5, [UIScreen mainScreen].bounds.size.width-20,hhPlaceCell+85);
        cell.headingTop.frame=CGRectMake(80, 5,SCREEN_WIDTH-160,hhPlaceCell+10);
        if (![str1 isEqualToString:@"nil"])
        {
            cell.headingTop.text=str1;
        }
        else
        {
            cell.userNameDesc.text=@"nothing";
        }
        
        cell.descriptionHeadingLbl.frame=CGRectMake(80,hhPlaceCell+15, SCREEN_WIDTH-80,15);
        cell.descriptionHeadingLbl.text=[NSString stringWithFormat:@"%@",[placeDataDict objectForKey:@"category"]];
        //--------
        cell.placeLabel.frame=CGRectMake(80,hhPlaceCell+30, SCREEN_WIDTH-80,15);

        cell.placeLabel.text=[NSString stringWithFormat:@"%@",[[placeDataDict objectForKey:@"location"] objectForKey:@"city"]];
        //-----
        cell.likeCountLabel.frame=CGRectMake(80,hhPlaceCell+50, SCREEN_WIDTH-80,15);
        cell.likeCountLabel.text =[NSString stringWithFormat:@"Likes %@",[placeDataDict objectForKey:@"likes"]];
        //----
        cell.peopleWhereLabel.frame=CGRectMake(80,hhPlaceCell+70, SCREEN_WIDTH-80,15);
        cell.peopleWhereLabel.text =[NSString stringWithFormat:@"Where How%@",[placeDataDict objectForKey:@"were_here_count"]];
        return cell;
    }
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(tableView==pageTableView)
    {
    
    NSError * error=nil;
    NSURLResponse * urlReponse=nil;
    NSIndexPath *indexPaths=[SingletonClass sharedState].selectedUserIndex;
    
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:indexPaths.row+indexPaths.section].token;
    
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
        
        
        NSString *accessToken1=token.tokenString;
        NSString *str1 =[self.idArray objectAtIndex:indexPath.row];
        
        NSString * urlStr=[NSString stringWithFormat:@"https://graph.facebook.com/%@/feed?access_token=%@",str1,accessToken1];
        
        NSString * urlStr2=[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        
        NSURL *url=[NSURL URLWithString:urlStr2];
        
        NSMutableURLRequest * request=[[NSMutableURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:50];
        
        
        NSData * data=[ NSURLConnection sendSynchronousRequest:request returningResponse:&urlReponse error:&error];
        
        
        if (data==nil) {
            NSLog(@" no data");
            return;
        }
        id response=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
        NSLog(@"reponse is %@",response);
        NSString *str2=  [self.idArray objectAtIndex:indexPath.row];
        
        if (self.disp) {
            self.disp=nil;
        }
        self.disp = [[PaggeInformationViewControllerFboard alloc]init];
        self.disp.idStr=str2;
        
        self.navigationController.navigationBarHidden=NO;
        [self.navigationController pushViewController:self.disp animated:YES];
        
    }
    }
    if(tableView==groupTableView)
    {
        NSError * error=nil;
        NSURLResponse * urlReponse=nil;
        NSString *accessToken1=[[NSUserDefaults standardUserDefaults]objectForKey:@"accessToken"];
        NSString *str1 =[self.grpIdArray objectAtIndex:indexPath.row];
        NSIndexPath *indexPaths=[SingletonClass sharedState].selectedUserIndex;
        FBSDKAccessToken *token = [SUCacheFboard itemForSlot:indexPaths.section+indexPaths.row].token;
        if (token) {
            
            [FBSDKAccessToken setCurrentAccessToken:token];
            FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"/%@",str1] parameters:nil];
            
            [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error3) {
                
                NSString *str11 = [result objectForKey:@"description"];
                NSString *str4 = [[result objectForKey:@"from"] objectForKey:@"name"];
                NSString *str3= [result objectForKey:@"icon"];
                NSString *str2 = [[result objectForKey:@"owner"]objectForKey:@"name"];
                NSString *str5 = [result objectForKey:@"id"];
                
                DisplayGroupViewControllerFboard *disp = [[DisplayGroupViewControllerFboard alloc]init];
                disp.email=str2;
                disp.ownerName = str4;
                disp.desc= str11;
                disp.iconUrl = str3;
                disp.idStr = str5;
                
                self.navigationController.navigationBarHidden=NO;
                [self.navigationController pushViewController:disp animated:YES];
            }];
            
            
            //
            NSString * urlStr=[NSString stringWithFormat:@"https://graph.facebook.com/%@/?access_token=%@",str1,token];
            
            NSString * urlStr2=[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            
            NSURL *url45=[NSURL URLWithString:urlStr2];
            
            NSMutableURLRequest * requestdf=[[NSMutableURLRequest alloc]initWithURL:url45 cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:50];
            
            
            NSData * data=[ NSURLConnection sendSynchronousRequest:requestdf returningResponse:&urlReponse error:&error];
            
            
            if (data==nil) {
                NSLog(@" no data");
                return;
            }
            id response=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
            NSLog(@"reponse is %@",response);
            
            
            
            NSString *str11 = [response objectForKey:@"description"];
            NSString *str2 = [response objectForKey:@"email"];
            NSString *str3= [response objectForKey:@"icon"];
            NSString *str4 = [[response objectForKey:@"owner"]objectForKey:@"name"];
            NSString *str5 = [response objectForKey:@"id"];
            
//            DisplayGroupViewControllerFboard *disp = [[DisplayGroupViewControllerFboard alloc]init];
//            disp.email=str2;
//            disp.ownerName = str4;
//            disp.desc= str11;
//            disp.iconUrl = str3;
//            disp.idStr = str5;
//            
//            [self presentViewController:disp animated:YES completion:nil]; //
        }
 
    }
    
    if(tableView==eventTableView)
    {
        //Going to Event Detail Page
        [self switchToEventDetails:(int)indexPath.row];
    }
}
-(CGFloat)calculateHeight:(NSString *)stringData width:(CGFloat)width
{
    UILabel * lblDescription=[[UILabel alloc]init];
    lblDescription.font=[UIFont systemFontOfSize:15];
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
-(void)switchToEventDetails:(int)rowValue
{
    NSError * error=nil;
    NSURLResponse * urlReponse=nil;
    NSIndexPath *indexPaths=[SingletonClass sharedState].selectedUserIndex;
    
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:indexPaths.row+indexPaths.section].token;
    
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
        
        
        NSString *accessToken1=token.tokenString;
        NSString *str1 =[self.eventIdArray objectAtIndex:rowValue];
        
        NSString * urlStr=[NSString stringWithFormat:@"https://graph.facebook.com/%@/feed?access_token=%@",str1,accessToken1];
        
        NSString * urlStr2=[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        
        NSURL *url=[NSURL URLWithString:urlStr2];
        
        NSMutableURLRequest * request=[[NSMutableURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:50];
        
        
        NSData * data=[ NSURLConnection sendSynchronousRequest:request returningResponse:&urlReponse error:&error];
        
        
        if (data==nil) {
            NSLog(@" no data");
            return;
        }
        id response=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
        NSLog(@"reponse is %@",response);
        NSString *str2=  [self.eventIdArray objectAtIndex:rowValue];
        
        if (self.dis) {
            self.dis=nil;
        }
        self.dis = [[EventinformationViewControllerFboard alloc]init];
        self.dis.eventidStr=str2;
        self.dis.allDataOfEventDetail=[response objectForKey:@"data"];
        self.navigationController.navigationBarHidden=NO;
        [self.navigationController pushViewController:self.dis animated:YES];
        
    }
}
#pragma mark Fetch User Data Services




-(void)fetchUserLikedPages{

    if (self.categoryArray&&self.nameArray&&self.idArray) {
        self.idArray=nil;
        self.nameArray=nil;
        self.categoryArray=nil;
    }
    
    self.categoryArray=[[NSMutableArray alloc] init];
    self.nameArray=[[NSMutableArray alloc] init];
    self.idArray=[[NSMutableArray alloc] init];
    
    BOOL connection= [[NSUserDefaults standardUserDefaults] boolForKey:@"ConnectionAvilable"];
    
    if (!connection) {
        UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No internet Connection" message:@"check your internet" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        return ;
    }
    
    
    
    
    NSIndexPath *indexpath=[SingletonClass sharedState].selectedUserIndex;
    
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:indexpath.row+indexpath.section].token;
    if (token) {
        NSString * constsearchQuery=[NSString stringWithFormat:@"search?q=%@&type=page",searchQuery];
        [FBSDKAccessToken setCurrentAccessToken:token];
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:constsearchQuery parameters:nil];
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
            //                              NSLog(@"result is %@",result);
            
            
            for (NSDictionary *dic in [result objectForKey:@"data"] ) {
                
                NSString *str1 = [dic objectForKey:@"category"];
                NSString *str2 = [dic objectForKey:@"name"];
                NSString *str3= [dic objectForKey:@"id"];
                
                if (str1) {
                    [self.categoryArray addObject:str1];
                }
                else{
                    [self.categoryArray addObject:@"nil"];
                }
                
                if (str2) {
                    [self.nameArray addObject:str2];
                }else{
                    [self.nameArray addObject:@"nil"];
                }
                
                if (str3) {
                    [self.idArray addObject:str3];
                }else{
                    [self.idArray addObject:@"nil"];
                }
                
            }
            dispatch_async(dispatch_get_main_queue(), ^{
                [ProgressHUD dismiss];
                [pageTableView reloadData];
            });
        }];
    }
}
// For Group Fetch Feeds
-(void)fetchGroupFeeds
{
    
    BOOL connection= [[NSUserDefaults standardUserDefaults] boolForKey:@"ConnectionAvilable"];
    
    if (!connection) {
        UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No internet Connection" message:@"check your internet" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        return ;
    }
    NSIndexPath *userIndex=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:userIndex.row+userIndex.section].token;
    if (token) {
        
        //        [FBSDKAccessToken setCurrentAccessToken:token];
        //        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:@"search?q=india&type=group" parameters:nil];
        
        NSString * constsearchQuery=[NSString stringWithFormat:@"search?q=%@&type=group",searchQuery];
        [FBSDKAccessToken setCurrentAccessToken:token];
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:constsearchQuery parameters:nil];
        
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
            
            groupData=[NSArray arrayWithArray:[result objectForKey:@"data"]];
            for (NSDictionary *dic in [result objectForKey:@"data"] ) {
                
                //----------
                
                //-----------
                NSString *str1 = [dic objectForKey:@"administrator"];
                NSString *str2 = [dic objectForKey:@"id"];
                NSString *str3 = [dic objectForKey:@"name"];
                NSString *str4 = [dic objectForKey:@"unread"];
                if (str1) {
                    [self.admistratorArray addObject:str1];
                }else{
                    [self.admistratorArray addObject:@"nil"];
                }
                
                if (str2) {
                    [self.grpIdArray addObject:str2];
                }else{
                    [self.grpIdArray addObject:@"nil"];
                }
                
                
                if (str3) {
                    [self.grpNameArray addObject:str3];
                }else{
                    [self.grpNameArray addObject:@"nil"];
                }
                
                if (str4) {
                    [self.unreadArray addObject:str4];
                }else{
                    [self.unreadArray addObject:@"nil"];
                }
                
                
            }
            //[self createFollowTable];
            //        NSLog(@"adminstrator array is %@",self.admistratorArray);
            //          NSLog(@"grpId array is %@",self.grpIdArray);
            //          NSLog(@"grp name array is %@",self.grpNameArray);
            //          NSLog(@"unreadArray array is %@",self.unreadArray);
            
            dispatch_async(dispatch_get_main_queue(), ^{
                [ProgressHUD dismiss];
                [groupTableView reloadData];
            });

        }];
    }
}

-(void)fetchevents
{
    BOOL connection = [[NSUserDefaults standardUserDefaults] boolForKey:@"ConnectionAvilable"];
    
    if(!connection)
    {
        UIAlertView *alert =[[UIAlertView alloc]initWithTitle:@"No Internet Connection" message:@"Check Your Connection" delegate:self cancelButtonTitle:@"ok" otherButtonTitles:nil, nil];
        [alert show];
        return;
    }
    
    NSIndexPath *userIndex = [SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:userIndex.row+userIndex.section ].token;
    
    
    if(token)
    {
        NSString * constsearchQuery = [NSString stringWithFormat:@"search?q=%@&type=event",searchQuery];
        [FBSDKAccessToken setCurrentAccessToken:token];
        
        
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc]initWithGraphPath:constsearchQuery parameters:nil];
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
            
            eventData=[NSArray arrayWithArray:[result objectForKey:@"data"]];
            for (NSDictionary *dic in [result objectForKey:@"data"] ) {
                
                //----------
                
                //-----------
                NSString *str1 = [dic objectForKey:@"administrator"];
                NSString *str2 = [dic objectForKey:@"id"];
                NSString *str3 = [dic objectForKey:@"name"];
                NSString *str4 = [dic objectForKey:@"unread"];
                if (str1) {
                    [self.eventadmistratorArray addObject:str1];
                }else{
                    [self.eventadmistratorArray addObject:@"nil"];
                }
                
                if (str2) {
                    [self.eventIdArray addObject:str2];
                }else{
                    [self.eventIdArray addObject:@"nil"];
                }
                
                
                if (str3) {
                    [self.eventNameArray addObject:str3];
                }else{
                    [self.eventNameArray addObject:@"nil"];
                }
                
                if (str4) {
                    [self.eventunreadArray addObject:str4];
                }else{
                    [self.eventunreadArray addObject:@"nil"];
                }
                
                
            }
            dispatch_async(dispatch_get_main_queue(), ^{
                [ProgressHUD dismiss];
                [eventTableView reloadData];
            });

        }];
    }
}
-(void)fetchPeople
{
    BOOL connection = [[NSUserDefaults standardUserDefaults] boolForKey:@"ConnectionAvilable"];
    
    if(!connection)
    {
        UIAlertView *alert =[[UIAlertView alloc]initWithTitle:@"No Internet Connection" message:@"Check Your Connection" delegate:self cancelButtonTitle:@"ok" otherButtonTitles:nil, nil];
        [alert show];
        return;
    }
    
    NSIndexPath *userIndex = [SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:userIndex.row+userIndex.section ].token;
    
    
    if(token)
    {
        NSString * constsearchQuery = [NSString stringWithFormat:@"search?q=%@&type=user",searchQuery];
        [FBSDKAccessToken setCurrentAccessToken:token];
        
        
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc]initWithGraphPath:constsearchQuery parameters:nil];
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
            
            peopleData=[NSArray arrayWithArray:[result objectForKey:@"data"]];
            for (NSDictionary *dic in [result objectForKey:@"data"] ) {
                
                //----------
                
                //-----------
                NSString *str1 = [dic objectForKey:@"administrator"];
                NSString *str2 = [dic objectForKey:@"id"];
                NSString *str3 = [dic objectForKey:@"name"];
                NSString *str4 = [dic objectForKey:@"unread"];
                if (str1) {
                    [self.eventadmistratorArray addObject:str1];
                }else{
                    [self.eventadmistratorArray addObject:@"nil"];
                }
                
                if (str2) {
                    [self.eventIdArray addObject:str2];
                }else{
                    [self.eventIdArray addObject:@"nil"];
                }
                
                
                if (str3) {
                    [self.eventNameArray addObject:str3];
                }else{
                    [self.eventNameArray addObject:@"nil"];
                }
                
                if (str4) {
                    [self.eventunreadArray addObject:str4];
                }else{
                    [self.eventunreadArray addObject:@"nil"];
                }
                
                
            }
            dispatch_async(dispatch_get_main_queue(), ^{
                [ProgressHUD dismiss];
                [peopleTableView reloadData];
            });

        }];
    }
}
-(void)fetchPlaces
{
    placeData=nil;
    [placeTableView reloadData];
    BOOL connection = [[NSUserDefaults standardUserDefaults] boolForKey:@"ConnectionAvilable"];
    
    if(!connection)
    {
        UIAlertView *alert =[[UIAlertView alloc]initWithTitle:@"No Internet Connection" message:@"Check Your Connection" delegate:self cancelButtonTitle:@"ok" otherButtonTitles:nil, nil];
        [alert show];
        return;
    }
    
    NSIndexPath *userIndex = [SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:userIndex.row+userIndex.section ].token;
    
    
    if(token)
    {
        NSString * constsearchQuery = [NSString stringWithFormat:@"search?q=%@&type=place",searchQuery];
        [FBSDKAccessToken setCurrentAccessToken:token];
        
        
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc]initWithGraphPath:constsearchQuery parameters:nil];
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error)
         {
            
            NSArray * tempArray=[NSArray arrayWithArray:[result objectForKey:@"data"]];
             NSMutableArray * mutuableTempArray=[[NSMutableArray alloc]init];
             dispatch_async(GCDBackgroundThread, ^{
                 @autoreleasepool {
             for (int i=0; i<tempArray.count; i++)
             {
                 [mutuableTempArray addObject:[self fetchLikesOfPlace:[tempArray objectAtIndex:i]]];
                 placeData=[NSArray arrayWithArray:mutuableTempArray];
                 dispatch_async(dispatch_get_main_queue(), ^(void){
                   
                     [self insertionOfRowInPlace];
                 });
             }
                     dispatch_async(dispatch_get_main_queue(), ^{
                         [ProgressHUD dismiss];
                     });
                 }});
        }];
    }
}
-(NSDictionary *)fetchLikesOfPlace:(NSDictionary*)presentData
{
    
    NSString * placeId=[presentData objectForKey:@"id"];
    NSMutableDictionary * tempDict=[[NSMutableDictionary alloc]initWithDictionary:presentData];

    NSError * error=nil;
    NSURLResponse * urlResponse=nil;
    NSURL * url=[NSURL  URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/?ids=%@&access_token=%@",placeId,[SingletonClass sharedState].accessTokenString]];
    
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
        [tempDict setObject:[[response objectForKey:placeId]objectForKey:@"likes"]  forKey:@"likes"];
        [tempDict setObject:[[response objectForKey:placeId]objectForKey:@"were_here_count"] forKey:@"were_here_count"];

    }
    return tempDict;
}
-(void)insertionOfRowInPlace
{
    int selectedRow = (int)placeData.count-1;

    NSLog(@"row pressed %d", selectedRow);
    [placeTableView beginUpdates];
    NSArray *paths = [NSArray arrayWithObject:
                      [NSIndexPath indexPathForRow:selectedRow inSection:0]];
    [placeTableView insertRowsAtIndexPaths:paths
                         withRowAnimation:UITableViewRowAnimationRight];
    [placeTableView endUpdates];
   
}
# pragma mark TextField Delegates

-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
    
    [textField resignFirstResponder];
    return YES;
}
-(void)textFieldDidBeginEditing:(UITextField *)textField
{
}
-(void)textFieldDidEndEditing:(UITextField *)textField
{
    searchQuery=searchTextField.text;
}
#pragma mark Progress HUD
-(void)addProgressHud
{
    dispatch_async(dispatch_get_main_queue(), ^(void){
        
        [ProgressHUD show:@"Loading..."];
    });
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
